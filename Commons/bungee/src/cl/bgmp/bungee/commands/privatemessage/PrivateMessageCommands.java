package cl.bgmp.bungee.commands.privatemessage;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PrivateMessageCommands {
  @Command(
      aliases = {"message", "msg", "pm", "tell"},
      desc = "Send a private message to another player.",
      usage = "<player> <msg>",
      min = 2)
  @CommandPermissions("commons.bungee.command.message")
  @CommandScopes("player")
  public static void message(final CommandContext args, CommandSender sender) {
    final ProxiedPlayer msgReceiver = CommonsBungee.get().getProxy().getPlayer(args.getString(0));
    if (msgReceiver == null) {
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.PLAYER_NOT_FOUND.getAsString())
              .color(ChatColor.RED)
              .build());
      return;
    }

    final ProxiedPlayer msgSender = (ProxiedPlayer) sender;
    final String message = args.getJoinedStrings(1);

    PrivateMessagesManager.sendMsg(msgSender, msgReceiver, message);
  }

  @Command(
      aliases = {"reply", "r"},
      desc = "Reply to the last message received.",
      usage = "<msg>",
      min = 1)
  @CommandPermissions("commons.bungee.command.reply")
  @CommandScopes("player")
  public static void reply(final CommandContext args, CommandSender sender) {
    PrivateMessagesManager.sendReply((ProxiedPlayer) sender, args.getJoinedStrings(0));
  }
}
