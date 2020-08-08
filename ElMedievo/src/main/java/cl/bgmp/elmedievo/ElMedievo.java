package cl.bgmp.elmedievo;

import cl.bgmp.elmedievo.commands.CoordsCommand;
import cl.bgmp.elmedievo.commands.ElMedievoCommand;
import cl.bgmp.elmedievo.commands.Reciper.Furnace.FurnaceCommand;
import cl.bgmp.elmedievo.commands.Reciper.ReciperCommand;
import cl.bgmp.elmedievo.listeners.EventManager;
import cl.bgmp.elmedievo.listeners.PlayerEvents;
import cl.bgmp.elmedievo.reciper.Furnace.FurnaceRecipesManager;
import cl.bgmp.elmedievo.teleport.TPAManager;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ElMedievo extends JavaPlugin {
  private static ElMedievo elMedievo;
  private TPAManager tpaManager;
  private FurnaceRecipesManager furnaceRecipesManager;
  private EventManager eventManager;
  private CommandsManager commands;
  private CommandsManagerRegistration commandRegistry;

  public static ElMedievo get() {
    return elMedievo;
  }

  public TPAManager getTpaManager() {
    return tpaManager;
  }

  public FurnaceRecipesManager getFurnaceRecipesManager() {
    return furnaceRecipesManager;
  }

  public EventManager getEventManager() {
    return eventManager;
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
    elMedievo = this;
    loadConfiguration();

    tpaManager = new TPAManager();
    furnaceRecipesManager = new FurnaceRecipesManager();
    eventManager = new EventManager();

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerCommands();
    registerEvents(new PlayerEvents());
  }

  private void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  @Override
  public void onDisable() {}

  public void registerEvents(Listener... listeners) {
    PluginManager pluginManager = Bukkit.getPluginManager();
    Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
  }

  private void registerCommands() {
    commandRegistry.register(CoordsCommand.class);
    commandRegistry.register(ReciperCommand.class);
    commandRegistry.register(FurnaceCommand.FurnaceParentCommand.class);
    commandRegistry.register(FurnaceCommand.class);
    commandRegistry.register(ElMedievoCommand.ElMedievoParentCommand.class);
    commandRegistry.register(ElMedievoCommand.class);
  }
}
