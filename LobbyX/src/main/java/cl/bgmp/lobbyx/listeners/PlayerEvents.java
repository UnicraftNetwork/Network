package cl.bgmp.lobbyx.listeners;

import cl.bgmp.lobbyx.Config;
import cl.bgmp.lobbyx.LobbyX;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    final Player player = event.getPlayer();

    player.teleport(config.getSpawn());
    player.setWalkSpeed(config.getWalkSpeed());
    player.setFlySpeed(config.getFlySpeed());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockBreak(BlockBreakEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockPlace(BlockPlaceEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    final Entity attacker = event.getDamager();
    final Entity damaged = event.getEntity();
    if (attacker instanceof Player || damaged instanceof Player) {
      if (attacker.hasPermission(config.getBypassPerm())) return;
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDamage(EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDamagedByBlock(EntityDamageByBlockEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemPickup(PlayerAttemptPickupItemEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemDrop(PlayerDropItemEvent event) {
    final Config config = LobbyX.get().getConfiguration();
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }
}
