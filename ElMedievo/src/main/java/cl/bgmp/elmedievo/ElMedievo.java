package cl.bgmp.elmedievo;

import cl.bgmp.elmedievo.Commands.CoordsCommand;
import cl.bgmp.elmedievo.Commands.Reciper.Furnace.FurnaceCommand;
import cl.bgmp.elmedievo.Commands.Reciper.ReciperCommand;
import cl.bgmp.elmedievo.Commands.SpawnCommand;
import cl.bgmp.elmedievo.Listeners.PlayerEvents;
import cl.bgmp.elmedievo.Listeners.WeatherEvents;
import cl.bgmp.elmedievo.Reciper.Furnace.FurnaceRecipesManager;
import cl.bgmp.elmedievo.Teleport.TPAManager;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Translations.Translations;
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
    loadConfiguaration();

    tpaManager = new TPAManager();
    furnaceRecipesManager = new FurnaceRecipesManager();

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerCommands();
    registerEvents(new WeatherEvents(), new PlayerEvents());
  }

  private void loadConfiguaration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  @Override
  public void onDisable() {}

  private void registerEvents(Listener... listeners) {
    PluginManager pluginManager = Bukkit.getPluginManager();
    Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
  }

  private void registerCommands() {
    commandRegistry.register(CoordsCommand.class);
    commandRegistry.register(ReciperCommand.class);
    commandRegistry.register(FurnaceCommand.FurnaceParentCommand.class);
    commandRegistry.register(FurnaceCommand.class);
    commandRegistry.register(SpawnCommand.class);
  }
}
