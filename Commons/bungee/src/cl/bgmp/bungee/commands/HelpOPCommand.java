package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import cl.bgmp.bungee.Permission;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HelpOPCommand {
  private final CommonsBungee commonsBungee;
  private final MultiResolver multiResolver;

  public HelpOPCommand(CommonsBungee commonsBungee, MultiResolver multiResolver) {
    this.commonsBungee = commonsBungee;
    this.multiResolver = multiResolver;
  }

  @Command(
      aliases = {"helpop"},
      desc = "Request help from a member of the staff.",
      usage = "<msg>",
      min = 1)
  @CommandPermissions("commons.bungee.command.helpop")
  @CommandScopes("player")
  public void helpop(final CommandContext args, CommandSender sender) {
    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final String message = args.getJoinedStrings(0);

    for (final ProxiedPlayer onlinePlayer : this.commonsBungee.getProxy().getPlayers()) {
      if (onlinePlayer.hasPermission(Permission.HELPOP_SEE.getNode())) {

        if (!onlinePlayer
            .getServer()
            .getInfo()
            .getName()
            .equals(player.getServer().getInfo().getName())) {
          onlinePlayer.sendMessage(
              new ComponentWrapper(ChatConstant.HELPOP_PREFIX.getAsString())
                  .append("[")
                  .color(ChatColor.WHITE)
                  .append(this.multiResolver.getClickableNameOf(player.getServer()))
                  .append("] ")
                  .color(ChatColor.WHITE)
                  .append(this.multiResolver.resolveProxiedPlayerNick(player))
                  .append(" ")
                  .append(ChatConstant.ARROW.getAsString())
                  .color(ChatColor.WHITE)
                  .append(" ")
                  .append(message)
                  .color(ChatColor.WHITE)
                  .build());
        } else {
          onlinePlayer.sendMessage(
              new ComponentWrapper(ChatConstant.HELPOP_PREFIX.getAsString())
                  .append(this.multiResolver.resolveProxiedPlayerNick(player))
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
        new ComponentWrapper(ChatConstant.HELPOP_SENT.getAsString())
            .color(ChatColor.GREEN)
            .build());
  }
}
