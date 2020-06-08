package cl.bgmp.bungee.commands.staffchat;

import cl.bgmp.bungee.BungeeMessages;
import cl.bgmp.bungee.ChatConstant;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffChatCommands {

  @Command(
      aliases = {"admin", "a", "staffchat", "sc"},
      desc = "Use the admin chat.",
      usage = "<msg>",
      help = "Use alone to lock the chat in admin mode. More arguments will send a message only.")
  @CommandPermissions("commons.bungee.command.admin")
  public static void admin(final CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          BungeeMessages.colourify(ChatColor.RED, ChatConstant.NO_CONSOLE.getAsTextComponent()));
      return;
    }

    if (args.argsLength() == 0) StaffChatManager.alternatePlayerChat((ProxiedPlayer) sender);
    else StaffChatManager.sendStaffChatMsg((ProxiedPlayer) sender, args.getJoinedStrings(0));
  }
}
