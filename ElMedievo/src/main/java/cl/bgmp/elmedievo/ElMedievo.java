package cl.bgmp.elmedievo;

import cl.bgmp.elmedievo.Commands.BackCommand;
import cl.bgmp.elmedievo.Commands.CoordsCommand;
import cl.bgmp.elmedievo.Commands.Reciper.Furnace.FurnaceCommand;
import cl.bgmp.elmedievo.Commands.Reciper.ReciperCommand;
import cl.bgmp.elmedievo.Commands.SpawnCommand;
import cl.bgmp.elmedievo.Commands.TPA.TPACancelCommand;
import cl.bgmp.elmedievo.Commands.TPA.TPACommand;
import cl.bgmp.elmedievo.Commands.TPA.TPADenyCommand;
import cl.bgmp.elmedievo.Commands.TPA.TPAList;
import cl.bgmp.elmedievo.Commands.TPA.TPAcceptCommand;
import cl.bgmp.elmedievo.Listeners.PlayerEvents;
import cl.bgmp.elmedievo.Listeners.WeatherEvents;
import cl.bgmp.elmedievo.Reciper.Furnace.FurnaceRecipesManager;
import cl.bgmp.elmedievo.Teleport.BackQueueManager;
import cl.bgmp.elmedievo.Teleport.TPAManager;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.elmedievo.Translations.Translator;
import cl.bgmp.utilsbukkit.Chat;
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
  private BackQueueManager backQueueManager;
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

  public BackQueueManager getBackQueueManager() {
    return backQueueManager;
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
          Chat.getStringAsException(Translator.translate(sender, ChatConstant.NO_PERMISSION)));
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(Chat.getStringAsException(exception.getUsage()));
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            Chat.getStringAsException(
                Translator.translate(sender, ChatConstant.NUMBER_STRING_EXCEPTION)));
      } else {
        sender.sendMessage(
            Chat.getStringAsException(Translator.translate(sender, ChatConstant.UNKNOWN_ERROR)));
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
    backQueueManager = new BackQueueManager();

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerCommands();
    registerEvents(new WeatherEvents(), new PlayerEvents());

    Translator.notifyMissingTranslations(this.getLogger());
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
    commandRegistry.register(TPACommand.class);
    commandRegistry.register(TPAcceptCommand.class);
    commandRegistry.register(TPADenyCommand.class);
    commandRegistry.register(TPACancelCommand.class);
    commandRegistry.register(TPAList.class);
    commandRegistry.register(ReciperCommand.class);
    commandRegistry.register(FurnaceCommand.FurnaceParentCommand.class);
    commandRegistry.register(FurnaceCommand.class);
    commandRegistry.register(SpawnCommand.class);
    commandRegistry.register(BackCommand.class);
  }
}
