package cl.bgmp.bungee;

import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.bungee.channels.ECChannel;
import cl.bgmp.bungee.channels.EveryoneChannel;
import cl.bgmp.bungee.channels.RefChannel;
import cl.bgmp.bungee.channels.StaffChannel;
import cl.bgmp.bungee.commands.HelpOPCommand;
import cl.bgmp.bungee.commands.LobbyCommand;
import cl.bgmp.bungee.commands.ServersCommand;
import cl.bgmp.bungee.commands.channelcommands.EventCoordChannelCommand;
import cl.bgmp.bungee.commands.channelcommands.EveryoneChannelCommand;
import cl.bgmp.bungee.commands.channelcommands.RefereeChannelCommand;
import cl.bgmp.bungee.commands.channelcommands.StaffChannelCommand;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessageCommands;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessagesManager;
import cl.bgmp.bungee.listeners.PlayerEvents;
import cl.bgmp.bungee.util.BungeeCommandsManager;
import cl.bgmp.bungee.util.CommandExecutor;
import cl.bgmp.bungee.util.CommandRegistration;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
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
  private ChannelsManager channelsManager;

  public static CommonsBungee get() {
    return commonsBungee;
  }

  public NetworkInfoProvider getNetworkInfoProvider() {
    return networkInfoProvider;
  }

  public ChannelsManager getChannelsManager() {
    return channelsManager;
  }

  @Override
  public void onCommand(CommandSender sender, String commandName, String[] args) {
    try {
      this.commands.execute(commandName, args, sender, sender);
    } catch (CommandPermissionsException e) {
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.NO_PERMISSION.getAsString())
              .color(ChatColor.RED)
              .build());
    } catch (MissingNestedCommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (CommandUsageException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (WrappedCommandException e) {
      if (e.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            new ComponentWrapper(ChatConstant.NUMBER_STRING_EXCEPTION.getAsString())
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new ComponentWrapper(ChatConstant.ERROR.getAsString()).color(ChatColor.RED).build());
        e.printStackTrace();
      }
    } catch (CommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
    }
  }

  @Override
  public void onEnable() {
    commonsBungee = this;

    commands = new BungeeCommandsManager();
    registrar = new CommandRegistration(this, this.getProxy().getPluginManager(), commands, this);

    PrivateMessagesManager.privateMessagesReplyRelations = new HashMap<>();
    networkInfoProvider = new NetworkInfoProvider(new HashSet<>(getProxy().getServers().values()));
    channelsManager =
        new ChannelsManager(
            new StaffChannel(), new EveryoneChannel(), new ECChannel(), new RefChannel());

    registerCommands(
        HelpOPCommand.class,
        LobbyCommand.class,
        PrivateMessageCommands.class,
        ServersCommand.class,
        EveryoneChannelCommand.class,
        StaffChannelCommand.class,
        EventCoordChannelCommand.class,
        RefereeChannelCommand.class);
    registerEvents(new PrivateMessagesManager(), new PlayerEvents());
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
