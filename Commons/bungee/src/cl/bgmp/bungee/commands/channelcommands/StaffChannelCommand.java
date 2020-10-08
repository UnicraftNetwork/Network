package cl.bgmp.bungee.commands.channelcommands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.channels.Channel;
import cl.bgmp.bungee.channels.ChannelName;
import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffChannelCommand {

  @Command(
      aliases = {"admin", "a"},
      desc = "Use the admin chat.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to admin. More arguments will just send the message through this channel.")
  @CommandPermissions("commons.bungee.command.admin")
  @CommandScopes("player")
  public static void admin(CommandContext args, CommandSender sender) {
    final Channel globalChannel =
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.EVERYONE);

    ChannelsManager.evalChannelCommand(
        args,
        sender,
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.STAFF),
        globalChannel);
  }
}
