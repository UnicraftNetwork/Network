package cl.bgmp.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class Util {

  /**
   * Resolves a player nickname throughout the Network.
   *
   * @param target player whom's nick will be resolved for the third party
   * @param enquirer third party requiring the resolved nick
   * @return formatted, colourful nickname if both target & enquirer share servers at that moment,
   *     or dark aqua colour nickname if not.
   */
  public static TextComponent resolveProxiedPlayerNick(
      ProxiedPlayer target, ProxiedPlayer enquirer) {
    final Server targetServer = target.getServer();
    final Server enquirerServer = enquirer.getServer();

    if (!targetServer.equals(enquirerServer))
      return BungeeMessages.colourify(ChatColor.DARK_AQUA, new TextComponent(target.getName()));
    else return new TextComponent(target.getDisplayName());
  }

  /**
   * Resolves a server name in the [Server] fashion.
   *
   * @param server Server which's name will be resolved
   * @return Formatted server name: [Server], or [Unknown] if server is null.
   */
  public static TextComponent resolveServerName(final Server server) {
    TextComponent networkServerName;
    networkServerName =
        server == null
            ? new TextComponent(
                ChatColor.WHITE + "[" + ChatColor.GOLD + "Unknown" + ChatColor.WHITE + "] ")
            : new TextComponent(
                ChatColor.WHITE
                    + "["
                    + ChatColor.GOLD
                    + server.getInfo().getName()
                    + ChatColor.WHITE
                    + "] ");
    return networkServerName;
  }
}
