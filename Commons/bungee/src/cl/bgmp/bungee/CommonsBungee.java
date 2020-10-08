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
import cl.bgmp.bungee.translations.AllTranslations;
import cl.bgmp.bungee.util.BungeeCommandsManager;
import cl.bgmp.bungee.util.CommandExecutor;
import cl.bgmp.bungee.util.CommandRegistration;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import java.util.Arrays;
import java.util.HashSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommonsBungee extends Plugin implements CommandExecutor<CommandSender> {
  private BungeeCommandsManager commandsManager;
  private CommandRegistration commandRegistration;

  private AllTranslations translations;
  private NetworkInfoProvider networkInfoProvider;
  private ChannelsManager channelsManager;

  private static CommonsBungee commonsBungee;

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
  public void onEnable() {
    commonsBungee = this;
    final PluginManager pm = this.getProxy().getPluginManager();

    this.commandsManager = new BungeeCommandsManager();
    this.commandRegistration = new CommandRegistration(this, pm, commandsManager, this);

    this.networkInfoProvider =
        new NetworkInfoProvider(new HashSet<>(getProxy().getServers().values()));

    this.channelsManager = new ChannelsManager();
    this.channelsManager.registerChannel(new StaffChannel());
    this.channelsManager.registerChannel(new EveryoneChannel());
    this.channelsManager.registerChannel(new ECChannel());
    this.channelsManager.registerChannel(new RefChannel());

    this.registerCommands();
    this.registerEvents(pm);
  }

  public void registerCommands() {
    this.registerCommand(HelpOPCommand.class);
    this.registerCommand(LobbyCommand.class);
    this.registerCommand(PrivateMessageCommands.class);
    this.registerCommand(ServersCommand.class);
    this.registerCommand(EveryoneChannelCommand.class);
    this.registerCommand(StaffChannelCommand.class);
    this.registerCommand(EventCoordChannelCommand.class);
    this.registerCommand(RefereeChannelCommand.class);
  }

  private void registerCommand(Class<?> clazz, Object... toInject) {
    if (toInject.length > 0) {
      this.commandsManager.setInjector(new SimpleInjector(toInject));
    }

    this.commandRegistration.register(clazz);
  }

  public void registerEvents(PluginManager pm) {
    pm.registerListener(this, new PrivateMessagesManager());
    pm.registerListener(this, new PlayerEvents());
  }

  // TODO: Remove
  public void registerEvents(Listener... listeners) {
    for (Listener listener : listeners) {
      getProxy().getPluginManager().registerListener(this, listener);
    }
  }

  @Override
  public void onCommand(CommandSender sender, String commandName, String[] args) {
    try {
      this.commandsManager.execute(commandName, args, sender, sender);
    } catch (ScopeMismatchException exception) {
      String[] scopes = exception.getScopes();
      if (!Arrays.asList(scopes).contains("player")) {
        sender.sendMessage(
            new ComponentWrapper("You must execute this command via server console!")
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new ComponentWrapper("You must be a player to execute this command!")
                .color(ChatColor.RED)
                .build());
      }
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
}
