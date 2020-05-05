package cl.bgmp.lobby.portals;

import cl.bgmp.lobby.Lobby;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Server;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class BungeePortal implements Listener {
  protected String id;
  protected Server server;
  protected Location location;
  protected PortalType type;

  public BungeePortal(String id, Server server, Location location, PortalType type) {
    this.id = id;
    this.server = server;
    this.location = location;
    this.type = type;
  }

  public Server getServer() {
    return server;
  }

  public Location getLocation() {
    return location;
  }

  public void sendPlayerToServer(Player player) {
    player.sendMessage(
        ChatColor.DARK_PURPLE
            + "Sending you to "
            + ChatColor.WHITE
            + "["
            + ChatColor.GOLD
            + server.getName()
            + ChatColor.WHITE
            + "]");
    Channels.sendPlayerToServer(Lobby.get(), player, server.getName());
  }

  public abstract void onPlayerClick(PlayerInteractEvent event);

  public abstract void onPlayerInteractAtNPC(NPCInteractEvent event);
}
