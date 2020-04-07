package cl.bgmp.elmedievo.Teleport;

import cl.bgmp.elmedievo.Translations.ChatConstant;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackQueueManager implements Listener {
  private HashMap<UUID, Location> backQueue = new HashMap<>();

  public BackQueueManager() {}

  public ImmutableMap<UUID, Location> getBackQueue() {
    return ImmutableMap.copyOf(backQueue);
  }

  public void teleportPlayer(final Player player) {
    if (!backQueue.containsKey(player.getUniqueId())) {
      player.sendMessage(
          ChatColor.RED + ChatConstant.NO_BACK_LOCATION.getTranslatedTo(player.getLocale()));
    } else {
      player.teleport(backQueue.get(player.getUniqueId()));
      player.sendMessage(
          ChatColor.RED + ChatConstant.TELEPORTED_BACK.getTranslatedTo(player.getLocale()));
    }
  }

  @EventHandler
  public void onPlayerDeath(final PlayerDeathEvent event) {
    final Player deadPlayer = event.getEntity();
    backQueue.put(deadPlayer.getUniqueId(), deadPlayer.getLocation());
  }
}
