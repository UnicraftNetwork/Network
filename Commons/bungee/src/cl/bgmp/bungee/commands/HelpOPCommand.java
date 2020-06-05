package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.BungeeMessages;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.Permission;
import cl.bgmp.bungee.Util;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HelpOPCommand {

  @Command(
      aliases = {"helpop"},
      desc = "Request help from a member of the staff.",
      usage = "<msg>",
      min = 1)
  @CommandPermissions("commons.bungee.command.helpop")
  public static void helpop(final CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          BungeeMessages.colourify(ChatColor.RED, ChatConstant.NO_CONSOLE.getAsTextComponent()));
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final String message = args.getJoinedStrings(0);

    // TODO: Probably optimise this
    for (final ProxiedPlayer onlinePlayer : CommonsBungee.get().getProxy().getPlayers()) {
      if (onlinePlayer.hasPermission(Permission.HELPOP_SEE.getNode())) {
        final ComponentBuilder componentBuilder =
            new ComponentBuilder(ChatConstant.HELPOP_PREFIX.getAsString());
        componentBuilder.append(Util.resolveServerName(player.getServer()).getText());
        componentBuilder.append(Util.resolveProxiedPlayerNick(player, onlinePlayer).getText());
        componentBuilder.append(" ");
        componentBuilder.append(ChatColor.WHITE + ChatConstant.ARROW.getAsString());
        componentBuilder.append(" ");
        componentBuilder.append(ChatColor.GRAY + message);
        onlinePlayer.sendMessage(componentBuilder.create());
      }
    }

    player.sendMessage(BungeeMessages.colourify(ChatColor.GREEN, ChatConstant.HELPOP_SENT.getAsTextComponent()));
  }
}
