package cl.bgmp.bungee;

import net.md_5.bungee.api.config.ServerInfo;

public class ServerDetails {
  private ServerInfo server;
  private int maxPlayers;

  public ServerDetails(ServerInfo server, int maxPlayers) {
    this.server = server;
    this.maxPlayers = maxPlayers;
  }

  public ServerInfo getServer() {
    return server;
  }

  public void setServer(ServerInfo server) {
    this.server = server;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }
}
