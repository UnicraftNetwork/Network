package cl.bgmp.bungee;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.md_5.bungee.api.config.ServerInfo;

public class NetworkInfoProvider {
  private CommonsBungee commonsBungee;
  private Collection<ServerInfo> servers;
  private Set<ServerDetails> serverDetails = new HashSet<>();

  public NetworkInfoProvider(CommonsBungee commonsBungee) {
    this.commonsBungee = commonsBungee;
    this.servers = this.commonsBungee.getProxy().getServers().values();

    for (ServerInfo server : servers) {
      serverDetails.add(new ServerDetails(server, -1));
    }

    this.commonsBungee
        .getProxy()
        .getScheduler()
        .schedule(this.commonsBungee, this::sync, 0L, 10L, TimeUnit.SECONDS);
  }

  /**
   * Retrieves the maximum amount of players a server can hold
   *
   * @param server The server to be checked for the amount of players
   * @return The max players of the server, or -1 if the passed server instance is null or not found
   */
  public int getServerMaxPlayers(ServerInfo server) {
    final ServerDetails serverDetails = getServerDetailsByServer(server);
    return serverDetails == null ? -1 : serverDetails.getMaxPlayers();
  }

  public ServerDetails getServerDetailsByServer(ServerInfo server) {
    return serverDetails.stream()
        .filter(serverDetail -> serverDetail.getServer().getName().equals(server.getName()))
        .findFirst()
        .orElse(null);
  }

  public void sync() {
    for (ServerInfo server : this.servers) {
      final AtomicInteger maxPlayers = new AtomicInteger();

      server.ping(
          (serverPing, throwable) ->
              maxPlayers.set(serverPing == null ? -1 : serverPing.getPlayers().getMax()));

      this.commonsBungee
          .getProxy()
          .getScheduler()
          .schedule(
              this.commonsBungee,
              () -> getServerDetailsByServer(server).setMaxPlayers(maxPlayers.get()),
              3L,
              TimeUnit.SECONDS);
    }
  }
}
