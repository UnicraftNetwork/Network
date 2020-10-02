package cl.bgmp.lobbyx;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.lobbyx.commands.LobbyXCommands;
import cl.bgmp.lobbyx.injection.BinderModule;
import cl.bgmp.lobbyx.listeners.BlockEvents;
import cl.bgmp.lobbyx.listeners.PlayerEvents;
import cl.bgmp.lobbyx.translations.AllTranslations;
import cl.bgmp.minecraft.util.commands.CommandsManager;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbyX extends JavaPlugin {
  private CommandsManager commandsManager = new BukkitCommandsManager();
  private LobbyXConfig config;
  private AllTranslations translations;

  @Inject private PlayerEvents playerEvents;
  @Inject private BlockEvents blockEvents;

  @Override
  public void onEnable() {
    this.loadConfig();
    this.translations = new AllTranslations();

    this.inject();
    this.registerCommands();

    this.registerEvents(this.blockEvents, this.playerEvents);
  }

  private void registerCommands() {
    this.registerCommand(LobbyXCommands.class, this, translations);
  }

  private void registerEvents(Listener... listeners) {
    PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, this);
    }
  }

  private void inject() {
    final BinderModule module = new BinderModule(this, this.config, this.translations);
    final Injector injector = module.createInjector();

    injector.injectMembers(this);
    injector.injectMembers(this.config);
    injector.injectMembers(this.translations);
  }

  private void loadConfig() {
    this.saveDefaultConfig();
    this.reloadConfig();
    if (config != null) return;

    this.getServer().getPluginManager().disablePlugin(this);
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    try {
      this.config = new LobbyXConfig(getConfig(), getDataFolder());
    } catch (RuntimeException e) {
      e.printStackTrace();
      this.getLogger()
          .severe(
              translations.get("misc.configuration.load.failed", getServer().getConsoleSender()));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

  private void registerCommand(Class<?> clazz, Object... toInject) {
    if (toInject.length > 0) this.commandsManager.setInjector(new SimpleInjector(toInject));
    else this.commandsManager.setInjector(null);
    CommandsManagerRegistration defaultRegistration =
        new CommandsManagerRegistration(this, this.commandsManager);

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
