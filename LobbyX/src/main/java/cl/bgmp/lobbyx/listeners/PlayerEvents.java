package cl.bgmp.lobbyx.listeners;

import cl.bgmp.lobbyx.Config;
import com.google.inject.Inject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

  private Config config;

  @Inject
  public PlayerEvents(Config config) {
    this.config = config;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    player.teleport(config.getSpawn());
    player.setWalkSpeed(config.getWalkSpeed());
    player.setFlySpeed(config.getFlySpeed());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerDamage(EntityDamageByEntityEvent event) {
    final Entity attacker = event.getDamager();
    final Entity damaged = event.getEntity();
    if (attacker instanceof Player || damaged instanceof Player) {
      if (attacker.hasPermission(config.getBypassPerm())) return;
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
    final Entity attacker = event.getDamager();
    if (attacker instanceof Player) {
      final Player player = (Player) attacker;
      if (player.hasPermission(config.getBypassPerm())) return;

      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
    final Entity remover = event.getRemover();
    if (remover instanceof Player) {
      final Player player = (Player) remover;
      if (player.hasPermission(config.getBypassPerm())) return;
    }

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerInteractItemFrames(PlayerInteractEntityEvent event) {
    final Entity entity = event.getRightClicked();
    if (!(entity instanceof ItemFrame)) return;

    final Player player = event.getPlayer();
    if (player.hasPermission(config.getBypassPerm())) return;

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onFoodLevelChange(FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDamage(EntityDamageEvent event) {
    EntityDamageEvent.DamageCause cause = event.getCause();
    if (cause != EntityDamageEvent.DamageCause.VOID) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    event.setDeathMessage(null);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemPickup(PlayerAttemptPickupItemEvent event) {
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onItemDrop(PlayerDropItemEvent event) {
    if (event.getPlayer().hasPermission(config.getBypassPerm())) return;
    event.setCancelled(true);
  }
}
