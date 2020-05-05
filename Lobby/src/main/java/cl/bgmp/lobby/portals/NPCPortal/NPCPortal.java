package cl.bgmp.lobby.portals.NPCPortal;

import cl.bgmp.lobby.Lobby;
import cl.bgmp.lobby.portals.BungeePortal;
import cl.bgmp.lobby.portals.PortalType;
import cl.bgmp.utilsbukkit.Server;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCPortal extends BungeePortal implements Listener {
  private NPC npc;

  public NPCPortal(String id, Server server, Location location, NPC npc) {
    super(id, server, location, PortalType.NPC);
    this.npc = npc;
    Lobby.get().registerEvents(this);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    new BukkitRunnable() {
      @Override
      public void run() {
        Bukkit.getScheduler().runTask(Lobby.get(), () -> npc.show(event.getPlayer()));
        this.cancel();
      }
    }.runTaskTimer(Lobby.get(), 20L, 0L);
  }

  @Override
  public void onPlayerClick(PlayerInteractEvent event) {}

  @Override
  @EventHandler
  public void onPlayerInteractAtNPC(NPCInteractEvent event) {
    if (!event.getNPC().equals(npc)) return;
    sendPlayerToServer(event.getWhoClicked());
  }
}
