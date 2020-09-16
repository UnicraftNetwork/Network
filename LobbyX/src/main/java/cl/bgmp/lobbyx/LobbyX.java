package cl.bgmp.lobbyx;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.butils.translations.Translations;
import cl.bgmp.lobbyx.commands.LobbyXCommands;
import cl.bgmp.lobbyx.listeners.BlockEvents;
import cl.bgmp.lobbyx.listeners.PlayerEvents;
import cl.bgmp.lobbyx.lobbygames.LobbyGamesManager;
import cl.bgmp.lobbyx.lobbygames.TargetShootingGame;
import cl.bgmp.lobbyx.translations.AllTranslations;
import cl.bgmp.minecraft.util.commands.CommandsManager;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class LobbyX extends JavaPlugin {
  private static LobbyX lobbyX;

  private CommandsManager commandsManager;
  private CommandsManagerRegistration defaultRegistration;

  private Config config;
  private Translations translations;

  private LobbyGamesManager lobbyGamesManager;

  public static LobbyX get() {
    return lobbyX;
  }

  public Config getConfiguration() {
    return config;
  }

  public Translations getTranslations() {
    return translations;
  }

  @Override
  public void onEnable() {
    lobbyX = this;

    this.saveDefaultConfig();
    this.reloadConfig();
    if (config == null) {
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    this.translations = new AllTranslations();

    this.commandsManager = new BukkitCommandsManager();
    this.defaultRegistration = new CommandsManagerRegistration(this, commandsManager);
    this.registerCommands(LobbyXCommands.class);

    this.lobbyGamesManager = new LobbyGamesManager();
    this.lobbyGamesManager.registerGames(new TargetShootingGame());

    this.registerEvents(new BlockEvents(), new PlayerEvents());
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    final boolean startup = config == null;
    try {
      this.config = new LobbyXConfig(getConfig(), getDataFolder());
    } catch (RuntimeException e) {
      e.printStackTrace();
      this.getLogger()
          .severe(
              translations.get("misc.configuration.load.failed", getServer().getConsoleSender()));
      return;
    }

    if (!startup)
      this.getLogger()
          .info(translations.get("misc.configuration.reloaded", getServer().getConsoleSender()));
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      Command command,
      @NotNull String label,
      @NotNull String[] args) {
    try {
      this.commandsManager.execute(command.getName(), args, sender, sender);
    } catch (ScopeMismatchException exception) {
      String[] scopes = exception.getScopes();
      if (!Arrays.asList(scopes).contains("player")) {
        sender.sendMessage(ChatColor.RED + translations.get("commands.no.player", sender));
      } else {
        sender.sendMessage(ChatColor.RED + translations.get("commands.no.console", sender));
      }
    } catch (CommandPermissionsException exception) {
      sender.sendMessage(ChatColor.RED + translations.get("commands.no.permission", sender));
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(
          ChatColor.RED + translations.get("commands.syntax.error", sender, exception.getUsage()));
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof NumberFormatException) {
        sender.sendMessage(ChatColor.RED + translations.get("commands.number.string", sender));
      } else {
        sender.sendMessage(translations.get("commands.unknown.error", sender));
        exception.printStackTrace();
      }
    } catch (CommandException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
    }
    return true;
  }

  private void registerCommands(Class<?>... classes) {
    for (Class<?> clazz : classes) {
      final Class<?>[] subclasses = clazz.getClasses();

      if (subclasses.length == 0) defaultRegistration.register(clazz);
      else {
        TabCompleter tabCompleter = null;
        Class<?> nestNode = null;
        for (Class<?> subclass : subclasses) {
          if (subclass.isAnnotationPresent(TabCompletion.class)) {
            try {
              tabCompleter = (TabCompleter) subclass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
              e.printStackTrace();
            }
          } else nestNode = subclass;
        }
        if (tabCompleter == null) defaultRegistration.register(subclasses[0]);
        else {
          CommandsManagerRegistration customRegistration =
              new CommandsManagerRegistration(this, this, tabCompleter, commandsManager);
          if (subclasses.length == 1) customRegistration.register(clazz);
          else customRegistration.register(nestNode);
        }
      }
    }
  }

  private void registerEvents(Listener... listeners) {
    PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, this);
    }
  }
}
