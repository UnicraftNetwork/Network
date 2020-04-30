package cl.bgmp.lobby;

import cl.bgmp.lobby.Listeners.BlockEvents;
import cl.bgmp.lobby.Listeners.PlayerEvents;
import cl.bgmp.lobby.Portals.PortalFactory;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Translations.Translations;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import net.jitse.npclib.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Lobby extends JavaPlugin {
  private static Lobby lobby;
  private PortalFactory portalFactory;
  private NPCLib npcLib;
  private CommandsManager commands;
  private CommandsManagerRegistration commandRegistry;

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
      this.commands.execute(command.getName(), args, sender, sender);
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

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerEvents(new BlockEvents(), new PlayerEvents());
    registerCommands();
  }

  @Override
  public void onDisable() {}

  public void registerCommands() {}

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
