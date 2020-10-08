package cl.bgmp.bungee.commands.channelcommands;

import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.channels.Channel;
import cl.bgmp.bungee.channels.ChannelName;
import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.CommandSender;

public class EveryoneChannelCommand {

  @Command(
      aliases = {"everyone", "e"},
      desc = "Everyone's channel.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to everyone. More arguments will just send the message through this channel.")
  @CommandScopes("player")
  public static void everyone(CommandContext args, CommandSender sender) {
    final Channel globalChannel =
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.EVERYONE);

    ChannelsManager.evalChannelCommand(args, sender, globalChannel, globalChannel);
  }
}
