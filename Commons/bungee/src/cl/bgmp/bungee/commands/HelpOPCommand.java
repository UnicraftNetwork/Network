package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Permission;
import cl.bgmp.bungee.Util;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
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
          new FlashComponent(ChatConstant.NO_CONSOLE.getAsString()).color(ChatColor.RED).build());
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final String message = args.getJoinedStrings(0);

    for (final ProxiedPlayer onlinePlayer : CommonsBungee.get().getProxy().getPlayers()) {
      if (onlinePlayer.hasPermission(Permission.HELPOP_SEE.getNode())) {

        if (!onlinePlayer
            .getServer()
            .getInfo()
            .getName()
            .equals(player.getServer().getInfo().getName())) {
          onlinePlayer.sendMessage(
              new FlashComponent(ChatConstant.HELPOP_PREFIX.getAsString())
                  .append("[")
                  .color(ChatColor.WHITE)
                  .append(Util.resolveServerName(player.getServer()))
                  .append("] ")
                  .color(ChatColor.WHITE)
                  .append(Util.resolveProxiedPlayerNick(player))
                  .append(" ")
                  .append(ChatConstant.ARROW.getAsString())
                  .color(ChatColor.WHITE)
                  .append(" ")
                  .append(message)
                  .color(ChatColor.WHITE)
                  .build());
        } else {
          onlinePlayer.sendMessage(
              new FlashComponent(ChatConstant.HELPOP_PREFIX.getAsString())
                  .append(Util.resolveProxiedPlayerNick(player))
                  .append(" ")
                  .append(ChatConstant.ARROW.getAsString())
                  .color(ChatColor.WHITE)
                  .append(" ")
                  .append(message)
                  .color(ChatColor.WHITE)
                  .build());
        }
      }
    }

    player.sendMessage(
        new FlashComponent(ChatConstant.HELPOP_SENT.getAsString()).color(ChatColor.GREEN).build());
  }
}
