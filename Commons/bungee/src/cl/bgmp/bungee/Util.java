package cl.bgmp.bungee;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
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
   * @param serverInfo Server's info which's name will be resolved
   * @return Formatted server name: [Server], or [Unknown] if server is null.
   */
  public static TextComponent resolveServerName(final ServerInfo serverInfo) {
    // TODO: Make server name clickable so players can directly teleport to it
    TextComponent networkServerName;
    networkServerName =
        serverInfo == null
            ? new TextComponent(
                ChatColor.WHITE + "[" + ChatColor.GOLD + "Unknown" + ChatColor.WHITE + "] ")
            : new TextComponent(
                ChatColor.WHITE
                    + "["
                    + ChatColor.GOLD
                    + serverInfo.getName()
                    + ChatColor.WHITE
                    + "] ");
    return networkServerName;
  }

  public static TextComponent resolveServerName(final Server server) {
    return resolveServerName(server.getInfo());
  }

  /**
   * Sends a given proxied player to a suitable lobby instance
   *
   * @param player The proxied player to be sent to a Lobby
   */
  public static void sendProxiedPlayerToLobby(final ProxiedPlayer player) {
    final Set<ServerInfo> servers =
        new HashSet<>(CommonsBungee.get().getProxy().getServers().values());
    final List<ServerInfo> availableLobbies =
        servers.stream()
            .filter(serverInfo -> serverInfo.getName().startsWith("Lobby"))
            .collect(Collectors.toList());

    if (availableLobbies.isEmpty()) {
      player.sendMessage(
          BungeeMessages.colourify(
              ChatColor.RED, ChatConstant.NO_LOBBIES_AVAILABLE.getAsTextComponent()));
    } else {
      availableLobbies.sort(Comparator.comparingInt(serverInfo -> serverInfo.getPlayers().size()));

      player.connect(availableLobbies.get(0));
      player.sendMessage(
          BungeeMessages.append(
              resolveServerName(availableLobbies.get(0)),
              ChatColor.DARK_PURPLE + ChatConstant.TELEPORTING.getAsString()));
    }
  }
}