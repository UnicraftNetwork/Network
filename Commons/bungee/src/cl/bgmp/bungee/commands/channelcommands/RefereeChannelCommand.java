package cl.bgmp.bungee.commands.channelcommands;

import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.channels.Channel;
import cl.bgmp.bungee.channels.ChannelName;
import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.CommandSender;

public class RefereeChannelCommand {

  @Command(
      aliases = "ref",
      desc = "Use the referee chat.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to referee. More arguments will just send the message through this channel.")
  @CommandPermissions("commons.bungee.command.referee")
  @CommandScopes("player")
  public static void referee(CommandContext args, CommandSender sender) {
    final Channel globalChannel =
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.EVERYONE);

    ChannelsManager.evalChannelCommand(
        args,
        sender,
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.REF),
        globalChannel);
  }
}
