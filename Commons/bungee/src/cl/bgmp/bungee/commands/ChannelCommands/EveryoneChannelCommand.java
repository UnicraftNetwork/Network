package cl.bgmp.bungee.commands.ChannelCommands;

import cl.bgmp.bungee.Channels.Channel;
import cl.bgmp.bungee.Channels.ChannelName;
import cl.bgmp.bungee.Channels.ChannelsManager;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.FlashComponent;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EveryoneChannelCommand {

  @Command(
      aliases = {"everyone", "e"},
      desc = "Everyone's channel.",
      usage = "<msg>",
      help =
          "Use alone to set your chat mode to everyone. More arguments will just send the message through this channel.")
  public static void everyone(CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          new FlashComponent(ChatConstant.NO_CONSOLE.getAsString()).color(ChatColor.RED).build());
      return;
    }

    final Channel globalChannel =
        CommonsBungee.get().getChannelsManager().getChannelByName(ChannelName.EVERYONE);

    ChannelsManager.evalChannelCommand(args, sender, globalChannel, globalChannel);
  }
}
