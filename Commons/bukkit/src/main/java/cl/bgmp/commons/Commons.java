package cl.bgmp.commons;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.commons.commands.ChatFormatterCommand;
import cl.bgmp.commons.commands.CommonsCommand;
import cl.bgmp.commons.commands.GamemodeCommand;
import cl.bgmp.commons.commands.RestartCommand;
import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.modules.ForceGamemodeModule;
import cl.bgmp.commons.modules.JoinQuitMessageModule;
import cl.bgmp.commons.modules.JoinToolsModule;
import cl.bgmp.commons.modules.ModuleManager;
import cl.bgmp.commons.modules.ModuleManagerImpl;
import cl.bgmp.commons.modules.NavigatorModule;
import cl.bgmp.commons.modules.RestartModule;
import cl.bgmp.commons.modules.TipsModule;
import cl.bgmp.commons.modules.WeatherModule;
import cl.bgmp.commons.translations.AllTranslations;
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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Commons extends JavaPlugin {
  private static Commons commons;

  private CommandsManager commandsManager;
  private CommandsManagerRegistration defaultRegistration;

  private Config config;
  private ModuleManagerImpl moduleManager;
  private AllTranslations translations;

  public static Commons get() {
    return commons;
  }

  public Config getConfiguration() {
    return config;
  }

  public AllTranslations getTranslations() {
    return translations;
  }

  public ModuleManager getModuleManager() {
    return moduleManager;
  }

  @Override
  public void onEnable() {
    commons = this;

    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bungeecord:main");

    this.saveDefaultConfig();
    this.reloadConfig();
    if (config == null) {
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    this.translations = new AllTranslations();

    this.commandsManager = new BukkitCommandsManager();
    this.defaultRegistration = new CommandsManagerRegistration(this, commandsManager);

    this.moduleManager = new ModuleManagerImpl();
    this.moduleManager.registerModules(
        new NavigatorModule(),
        new JoinToolsModule(),
        new ForceGamemodeModule(),
        new ChatFormatModule(),
        new WeatherModule(),
        new JoinQuitMessageModule(),
        new RestartModule(),
        new TipsModule(getLogger()));
    this.moduleManager.loadModules();

    this.registerCommands(
        ChatFormatterCommand.class,
        CommonsCommand.class,
        GamemodeCommand.class,
        RestartCommand.class);

    this.registerEvents();
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    final boolean startup = config == null;
    try {
      this.config = new CommonsConfig(getConfig(), getDataFolder());
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

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, this);
    }
  }

  public void unregisterEvents(Listener... listeners) {
    for (Listener listener : listeners) {
      HandlerList.unregisterAll(listener);
    }
  }
}
