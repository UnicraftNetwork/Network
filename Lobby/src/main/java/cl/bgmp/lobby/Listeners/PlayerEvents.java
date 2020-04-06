package cl.bgmp.lobby.Listeners;

import cl.bgmp.lobby.Config;
import cl.bgmp.utilsbukkit.Locations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    player.teleport(
        Locations.getOffsetLocation(Config.Spawn.getLocation(), Config.Spawn.getSpreadRatio()));
    player.setWalkSpeed(Config.Spawn.getWalkSpeed());
    player.setFlySpeed(Config.Spawn.getFlySpeed());
  }

   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockBreak(BlockBreakEvent event) {
    if (event.getPlayer().hasPermission(Config.Network.getBypassPermission())) return;
    event.setCancelled(true);
   }

   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getPlayer().hasPermission(Config.Network.getBypassPermission())) return;
    event.setCancelled(true);
   }

   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
       final Entity attacker = event.getDamager();
       final Entity damaged = event.getEntity();
       if (damaged instanceof ArmorStand
               || damaged instanceof ItemFrame
               || damaged instanceof Boat
       ) return;
       if (attacker instanceof Player || damaged instanceof Player) event.setCancelled(true);
   }
}
