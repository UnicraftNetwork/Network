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
      ProxiedPlayer target, ProxiedPlayer enquirer) {
    final Server targetServer = target.getServer();
    final Server enquirerServer = enquirer.getServer();

    if (!targetServer.getInfo().getName().equals(enquirerServer.getInfo().getName())) {
      return new FlashComponent(target.getName()).color(ChatColor.DARK_AQUA);
    } else return new FlashComponent(target.getDisplayName());
  }

  public static FlashComponent resolveServerName(final Server server) {
    FlashComponent serverName;
    serverName =
        server == null
            ? new FlashComponent("Unknown").color(ChatColor.GOLD)
            : resolveServerName(server.getInfo());
    return serverName;
  }

  public static FlashComponent resolveServerName(final @NotNull ServerInfo serverInfo) {
    return new FlashComponent(serverInfo.getName())
        .color(ChatColor.GOLD)
        .hoverText(new FlashComponent("Click to connect").color(ChatColor.YELLOW).build())
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
