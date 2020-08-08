package cl.bgmp.elmedievo.sound;

import java.util.Collection;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {

  public SoundManager() {}

  public static void playSoundAtPlayer(Player player, Sound sound) {
    player.playSound(player.getLocation(), sound, 1F, 1F);
  }

  public static void playSoundAtPlayers(Sound sound, Collection<? extends Player> players) {
    for (Player player : players) {
      playSoundAtPlayer(player, sound);
    }
  }

  public static void playSoundAtPlayer(Player player, Sound sound, float volume, float pitch) {
    player.playSound(player.getLocation(), sound, volume, pitch);
  }
}
