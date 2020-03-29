package cl.bgmp.elmedievo.Sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {

  public SoundManager() {}

  public static void playSoundAtPlayer(Player player, Sound sound) {
    player.playSound(player.getLocation(), sound, 0.5F, 0.5F);
  }

  public static void playSoundAtPlayer(Player player, Sound sound, float volume, float pitch) {
    player.playSound(player.getLocation(), sound, volume, pitch);
  }
}
