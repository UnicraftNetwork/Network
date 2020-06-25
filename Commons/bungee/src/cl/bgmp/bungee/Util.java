package cl.bgmp.bungee;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;

public class Util {

  /**
   * Resolves a player nickname throughout the Network.
   *
   * @param target player owner of the queried nickname
   * @param enquirer third party requiring the resolved nick
   * @return formatted, colourful nickname if both target & enquirer share servers at that moment,
   *     or dark aqua colour nickname if not.
   */
  public static FlashComponent resolveProxiedPlayerNick(
      ProxiedPlayer target /* ProxiedPlayer enquirer */) {
    // final Server targetServer = target.getServer();
    // final Server enquirerServer = enquirer.getServer();

    return new FlashComponent(target.getName()).color(ChatColor.DARK_AQUA);

    /* FIXME: This below requires a reliable method to set the user's nickname colours and such. Commenting until that's worked out.
    if (!targetServer.getInfo().getName().equals(enquirerServer.getInfo().getName())) {
      return new FlashComponent(target.getName()).color(ChatColor.DARK_AQUA);
    } else return new FlashComponent(target.getDisplayName());
    */
  }

  public static FlashComponent resolveServerName(final Server server) {
    FlashComponent serverName;
    serverName =
        server == null
            ? new FlashComponent("Unknown").color(ChatColor.GOLD)
            : resolveServerName(server.getInfo());
    return serverName;
  }

  /**
   * Resolves a server name, with a hover text of "[Server] Click to connect" & a click command to
   * connect to it
   *
   * @param serverInfo The server with the name to be resolved
   * @return The server name "Server" with hover and click events applied
   */
  public static FlashComponent resolveServerName(final @NotNull ServerInfo serverInfo) {
    return new FlashComponent(serverInfo.getName())
        .color(ChatColor.GOLD)
        .hoverText(
            new FlashComponent("[")
                .color(ChatColor.WHITE)
                .append(serverInfo.getName())
                .color(ChatColor.GOLD)
                .append("] ")
                .color(ChatColor.WHITE)
                .append("Click to connect")
                .color(ChatColor.GREEN)
                .build())
        .clickCommand("server " + serverInfo.getName());
  }

  /**
   * Resolves the lobby with the least amount of players, and deems it suitable to be returned
   *
   * @return The suitable lobby instance, or null if not found
   */
  public static ServerInfo resolveSuitableLobby() {
    final Set<ServerInfo> servers =
        new HashSet<>(CommonsBungee.get().getProxy().getServers().values());
    final List<ServerInfo> availableLobbies =
        servers.stream()
            .filter(serverInfo -> serverInfo.getName().startsWith("Lobby"))
            .collect(Collectors.toList());

    if (availableLobbies.isEmpty()) {
      return null;
    } else {
      availableLobbies.sort(Comparator.comparingInt(serverInfo -> serverInfo.getPlayers().size()));
      return availableLobbies.get(0);
    }
  }
}
