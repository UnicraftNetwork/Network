package cl.bgmp.bungee;

import cl.bgmp.bungee.commands.HelpOPCommand;
import cl.bgmp.bungee.commands.LobbyCommand;
import cl.bgmp.bungee.commands.ServersCommand;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessageCommands;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessagesManager;
import cl.bgmp.bungee.commands.staffchat.StaffChatCommands;
import cl.bgmp.bungee.commands.staffchat.StaffChatManager;
import cl.bgmp.bungee.listeners.PlayerEvents;
import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import java.util.HashMap;
import java.util.HashSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class CommonsBungee extends Plugin implements CommandExecutor<CommandSender> {
  private static CommonsBungee commonsBungee;
  private BungeeCommandsManager commands;
  private CommandRegistration registrar;
  private NetworkInfoProvider networkInfoProvider;

  public static CommonsBungee get() {
    return commonsBungee;
  }

  public NetworkInfoProvider getNetworkInfoProvider() {
    return networkInfoProvider;
  }

  @Override
  public void onCommand(CommandSender sender, String commandName, String[] args) {
    try {
      this.commands.execute(commandName, args, sender, sender);
    } catch (CommandPermissionsException e) {
      sender.sendMessage(
          new FlashComponent(ChatConstant.NO_PERMISSION.getAsString())
              .color(ChatColor.RED)
              .build());
    } catch (MissingNestedCommandException e) {
      sender.sendMessage(new FlashComponent(e.getUsage()).color(ChatColor.RED).build());
    } catch (CommandUsageException e) {
      sender.sendMessage(new FlashComponent(e.getMessage()).color(ChatColor.RED).build());
      sender.sendMessage(new FlashComponent(e.getUsage()).color(ChatColor.RED).build());
    } catch (WrappedCommandException e) {
      if (e.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            new FlashComponent(ChatConstant.NUMBER_STRING_EXCEPTION.getAsString())
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new FlashComponent(ChatConstant.ERROR.getAsString()).color(ChatColor.RED).build());
        e.printStackTrace();
      }
    } catch (CommandException e) {
      sender.sendMessage(new FlashComponent(e.getMessage()).color(ChatColor.RED).build());
    }
  }

  @Override
  public void onEnable() {
    commonsBungee = this;

    commands = new BungeeCommandsManager();
    registrar = new CommandRegistration(this, this.getProxy().getPluginManager(), commands, this);

    PrivateMessagesManager.privateMessagesReplyRelations = new HashMap<>();
    StaffChatManager.playerChatStates = new HashMap<>();
    networkInfoProvider = new NetworkInfoProvider(new HashSet<>(getProxy().getServers().values()));

    registerCommands(
        HelpOPCommand.class,
        LobbyCommand.class,
        PrivateMessageCommands.class,
        StaffChatCommands.class,
        ServersCommand.class);
    registerEvents(new PrivateMessagesManager(), new StaffChatManager(), new PlayerEvents());
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
