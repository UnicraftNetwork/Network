package cl.bgmp.bungee;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/** Resolves information based on network status */
public class MultiResolver {
  private final CommonsBungee commonsBungee;

  public MultiResolver(CommonsBungee commonsBungee) {
    this.commonsBungee = commonsBungee;
  }

  /**
   * Resolves a player nickname throughout the Network.
   *
   * @param target player owner of the queried nickname
   * @param enquirer third party requiring the resolved nick
   * @return formatted, colourful nickname if both target & enquirer share servers at that moment,
   *     or dark aqua colour nickname if not.
   */
  public ComponentWrapper resolveProxiedPlayerNick(
      ProxiedPlayer target /* ProxiedPlayer enquirer */) {
    // final Server targetServer = target.getServer();
    // final Server enquirerServer = enquirer.getServer();

    return new ComponentWrapper(target.getName()).color(ChatColor.DARK_AQUA);

    /* FIXME: This below requires a reliable method to set the user's nickname colours and such. Commenting until that's worked out.
    if (!targetServer.getInfo().getName().equals(enquirerServer.getInfo().getName())) {
      return new FlashComponent(target.getName()).color(ChatColor.DARK_AQUA);
    } else return new FlashComponent(target.getDisplayName());
    */
  }

  public ComponentWrapper resolveServerName(final Server server) {
    final ComponentWrapper serverName;
    serverName =
        server == null
            ? new ComponentWrapper("Unknown").color(ChatColor.GOLD)
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
  public ComponentWrapper resolveServerName(ServerInfo serverInfo) {
    return new ComponentWrapper(serverInfo.getName())
        .color(ChatColor.GOLD)
        .hoverText(
            new ComponentWrapper("[")
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
  public ServerInfo resolveSuitableLobby() {
    final Collection<ServerInfo> serverInfoCollection =
        commonsBungee.getProxy().getServers().values();
    final Set<ServerInfo> servers = new HashSet<>(serverInfoCollection);
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
