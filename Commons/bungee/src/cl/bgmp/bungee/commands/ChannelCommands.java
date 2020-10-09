package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.channels.Channel;
import cl.bgmp.bungee.channels.ChannelName;
import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.CommandSender;

public class ChannelCommands {
  private final ChannelsManager channelsManager;

  public ChannelCommands(ChannelsManager channelsManager) {
    this.channelsManager = channelsManager;
  }

  @Command(
      aliases = {"everyone", "e"},
      desc = "Everyone's channel.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to everyone. More arguments will just send the message through this channel.")
  @CommandScopes("player")
  public void everyone(CommandContext args, CommandSender sender) {
    final Channel globalChannel = this.channelsManager.getChannelByName(ChannelName.EVERYONE);

    this.channelsManager.evalChannelCommand(args, sender, globalChannel, globalChannel);
  }

  @Command(
      aliases = {"ec", "eventcoord"},
      desc = "Use the event coordinator.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to event coordinator. More arguments will just send the message through this channel.")
  @CommandPermissions("commons.bungee.command.ec")
  @CommandScopes("player")
  public void ec(CommandContext args, CommandSender sender) {
    final Channel globalChannel = this.channelsManager.getChannelByName(ChannelName.EVERYONE);

    this.channelsManager.evalChannelCommand(
        args, sender, this.channelsManager.getChannelByName(ChannelName.EC), globalChannel);
  }

  @Command(
      aliases = "ref",
      desc = "Use the referee chat.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to referee. More arguments will just send the message through this channel.")
  @CommandPermissions("commons.bungee.command.referee")
  @CommandScopes("player")
  public void referee(CommandContext args, CommandSender sender) {
    final Channel globalChannel = this.channelsManager.getChannelByName(ChannelName.EVERYONE);

    this.channelsManager.evalChannelCommand(
        args, sender, this.channelsManager.getChannelByName(ChannelName.REF), globalChannel);
  }

  @Command(
      aliases = {"admin", "a"},
      desc = "Use the admin chat.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to admin. More arguments will just send the message through this channel.")
  @CommandPermissions("commons.bungee.command.admin")
  @CommandScopes("player")
  public void admin(CommandContext args, CommandSender sender) {
    final Channel globalChannel = this.channelsManager.getChannelByName(ChannelName.EVERYONE);

    this.channelsManager.evalChannelCommand(
        args, sender, this.channelsManager.getChannelByName(ChannelName.STAFF), globalChannel);
  }
}
