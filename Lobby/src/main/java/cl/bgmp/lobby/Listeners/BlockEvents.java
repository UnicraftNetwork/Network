package cl.bgmp.lobby.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class BlockEvents implements Listener {
  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockPhysics(final BlockPhysicsEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockBurn(final BlockBurnEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockSpread(final BlockSpreadEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockMelt(final BlockFromToEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockFade(final BlockFadeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBlockForm(final BlockFormEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onLeaveDecay(final LeavesDecayEvent event) {
    event.setCancelled(true);
  }
}
