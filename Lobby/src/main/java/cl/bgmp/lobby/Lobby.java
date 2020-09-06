package cl.bgmp.lobby;

import cl.bgmp.lobby.commands.LobbyCommand;
import cl.bgmp.lobby.listeners.BlockEvents;
import cl.bgmp.lobby.listeners.PlayerEvents;
import cl.bgmp.lobby.portals.PortalFactory;
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
import net.jitse.npclib.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Lobby extends JavaPlugin {
  private static Lobby lobby;
  private PortalFactory portalFactory;
  private NPCLib npcLib;

  @SuppressWarnings("rawtypes")
  private CommandsManager commandsManager;

  private CommandsManagerRegistration defaultRegistration;

  public static Lobby get() {
    return lobby;
  }

  public NPCLib getNPCLib() {
    return npcLib;
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
    lobby = this;

    // Registers Bungee's outgoing channel used for bungee-related things
    Channels.registerBungeeToPlugin(this);

    // Instantiates the NPC library used to create NPC portals
    npcLib = new NPCLib(this);

    loadConfiguration();

    portalFactory = new PortalFactory();
    portalFactory.loadPortals();

    registerEvents(new BlockEvents(), new PlayerEvents());

    commandsManager = new BukkitCommandsManager();
    defaultRegistration = new CommandsManagerRegistration(this, commandsManager);

    registerCommands(LobbyCommand.class);
  }

  @Override
  public void onDisable() {}

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

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
