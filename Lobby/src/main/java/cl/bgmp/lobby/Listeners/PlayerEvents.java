package cl.bgmp.lobby.Listeners;

import cl.bgmp.lobby.Settings;
import cl.bgmp.utilsbukkit.Locations;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    player.teleport(
        Locations.getOffsetLocation(Settings.Spawn.getLocation(), Settings.Spawn.getSpreadRatio()));
    player.setWalkSpeed(Settings.Spawn.getWalkSpeed());
    player.setFlySpeed(Settings.Spawn.getFlySpeed());
  }
}
