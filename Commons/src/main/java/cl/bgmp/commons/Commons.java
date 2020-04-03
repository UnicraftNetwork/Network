package cl.bgmp.commons;

import cl.bgmp.commons.Chat.ChatFormatter;
import cl.bgmp.commons.Commands.ChatFormatterCommand;
import cl.bgmp.commons.Navigator.Navigator;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Chat;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Commons extends JavaPlugin {
  private static Commons commons;
  private Navigator navigator;
  private ChatFormatter chatFormatter;
  private CommandsManager commands;
  private CommandsManagerRegistration commandRegistry;

  public static Commons get() {
    return commons;
  }

  public Navigator getNavigator() {
    return navigator;
  }

  public ChatFormatter getChatFormatter() {
    return chatFormatter;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      Command command,
      @NotNull String label,
      @NotNull String[] args) {
    try {
      this.commands.execute(command.getName(), args, sender, sender);
    } catch (CommandPermissionsException exception) {
      sender.sendMessage(Chat.getStringAsException("You do not have permission"));
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(Chat.getStringAsException(exception.getUsage()));
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof NumberFormatException) {
        sender.sendMessage(Chat.getStringAsException("Expected a number. Received string instead"));
      } else {
        sender.sendMessage(Chat.getStringAsException("An unknown error has occurred."));
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

    navigator = new Navigator();
    chatFormatter = new ChatFormatter(getLogger());

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerCommands();
    registerEvents();
  }

  @Override
  public void onDisable() {}

  public void registerCommands() {
    commandRegistry.register(ChatFormatterCommand.ChatFormatterParentCommand.class);
    commandRegistry.register(ChatFormatterCommand.class);
  }

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
