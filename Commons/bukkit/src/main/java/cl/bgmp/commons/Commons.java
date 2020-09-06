package cl.bgmp.commons;

import cl.bgmp.commons.commands.ChatFormatterCommand;
import cl.bgmp.commons.commands.CommonsCommand;
import cl.bgmp.commons.commands.GamemodeCommand;
import cl.bgmp.commons.commands.PurchaseNotificationCommand;
import cl.bgmp.commons.commands.RestartCommand;
import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.modules.ForceGamemodeModule;
import cl.bgmp.commons.modules.JoinQuitMessageModule;
import cl.bgmp.commons.modules.JoinToolsModule;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.commons.modules.ModuleManager;
import cl.bgmp.commons.modules.NavigatorModule;
import cl.bgmp.commons.modules.RestartModule;
import cl.bgmp.commons.modules.TipsModule;
import cl.bgmp.commons.modules.WeatherModule;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.annotations.TabCompletion;
import com.sk89q.minecraft.util.commands.exceptions.CommandException;
import com.sk89q.minecraft.util.commands.exceptions.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.exceptions.CommandUsageException;
import com.sk89q.minecraft.util.commands.exceptions.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.exceptions.ScopeMismatchException;
import com.sk89q.minecraft.util.commands.exceptions.WrappedCommandException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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

public final class Commons extends JavaPlugin implements ModuleManager {
  private static Commons commons;

  @SuppressWarnings("rawtypes")
  private CommandsManager commandsManager;

  private CommandsManagerRegistration defaultRegistration;

  private Set<Module> modules = new HashSet<>();

  public static Commons get() {
    return commons;
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
        sender.sendMessage(
            Chat.getStringAsException(Translations.get("commands.no.player", sender)));
      } else {
        sender.sendMessage(
            Chat.getStringAsException(Translations.get("commands.no.console", sender)));
      }
    } catch (CommandPermissionsException exception) {
      sender.sendMessage(
          Chat.getStringAsException(Translations.get("commands.no.permission", sender)));
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(Chat.getStringAsException(exception.getUsage()));
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            Chat.getStringAsException(Translations.get("misc.number.string.exception", sender)));
      } else {
        sender.sendMessage(
            Chat.getStringAsException(Translations.get("misc.unknown.error", sender)));
        exception.printStackTrace();
      }
    } catch (CommandException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
    }
    return true;
  }

  @Override
  public void onEnable() {
    commons = this;
    loadConfiguration();

    Channels.registerBungeeToPlugin(this);

    commandsManager = new BukkitCommandsManager();
    defaultRegistration = new CommandsManagerRegistration(this, commandsManager);

    registerModules(
        new NavigatorModule(),
        new JoinToolsModule(),
        new ForceGamemodeModule(),
        new ChatFormatModule(),
        new WeatherModule(),
        new JoinQuitMessageModule(),
        new RestartModule(),
        new TipsModule(getLogger()));

    registerCommands(
        ChatFormatterCommand.class,
        CommonsCommand.class,
        GamemodeCommand.class,
        PurchaseNotificationCommand.class,
        RestartCommand.class);
    loadModules();

    registerEvents();
  }

  @Override
  public void onDisable() {}

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void unregisterEvents(Listener... listeners) {
    for (Listener listener : listeners) HandlerList.unregisterAll(listener);
  }

  private void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  @Override
  public void registerModules(Module... modules) {
    this.modules.addAll(Arrays.asList(modules));
  }

  @Override
  public void loadModules() {
    this.modules.stream()
        .filter(Module::isEnabled)
        .collect(Collectors.toSet())
        .forEach(Module::load);
  }

  @Override
  public void reloadModules() {
    this.modules.forEach(Module::unload);
    this.loadModules();
  }

  @Override
  public Module getModule(ModuleId id) {
    return this.modules.stream()
        .filter(module -> module.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public void registerCommands(Class<?>... classes) {
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
}
