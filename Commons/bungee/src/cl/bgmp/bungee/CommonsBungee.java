package cl.bgmp.bungee;

import cl.bgmp.bungee.commands.HelpOPCommand;
import cl.bgmp.bungee.commands.ServerCommands;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessageCommands;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessagesManager;
import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import java.util.HashMap;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class CommonsBungee extends Plugin implements CommandExecutor<CommandSender> {
  private static CommonsBungee commonsBungee;
  private static HashMap<String, String> privateMessagesReplyRelations;

  private BungeeCommandsManager commands;
  private CommandRegistration registrar;

  public static CommonsBungee get() {
    return commonsBungee;
  }

  public static HashMap<String, String> getPrivateMessagesReplyRelations() {
    return privateMessagesReplyRelations;
  }

  @Override
  public void onCommand(CommandSender sender, String commandName, String[] args) {
    try {
      this.commands.execute(commandName, args, sender, sender);
    } catch (CommandPermissionsException e) {
      BungeeMessages.commandPermissionsException(sender);
    } catch (MissingNestedCommandException e) {
      BungeeMessages.nestedCommandUsageException(sender, e.getUsage());
    } catch (CommandUsageException e) {
      BungeeMessages.commandUsageException(sender, e.getMessage(), e.getUsage());
    } catch (WrappedCommandException e) {
      if (e.getCause() instanceof NumberFormatException) {
        BungeeMessages.numberFormatException(sender);
      } else {
        BungeeMessages.consoleError(sender);
        e.printStackTrace();
      }
    } catch (CommandException e) {
      BungeeMessages.commandException(sender, e.getMessage());
    }
  }

  @Override
  public void onEnable() {
    commonsBungee = this;

    commands = new BungeeCommandsManager();
    registrar = new CommandRegistration(this, this.getProxy().getPluginManager(), commands, this);

    privateMessagesReplyRelations = new HashMap<>();

    registerCommands(HelpOPCommand.class, ServerCommands.class, PrivateMessageCommands.class);
    registerEvents(new PrivateMessagesManager());
  }

  private void registerCommands(Class<?>... commandClasses) {
    for (Class<?> commandClass : commandClasses) {
      registrar.register(commandClass);
    }
  }

  public void registerEvents(Listener... listeners) {
    for (Listener listener : listeners) {
      getProxy().getPluginManager().registerListener(this, listener);
    }
  }
}
