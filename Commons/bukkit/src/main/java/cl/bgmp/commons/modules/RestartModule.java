package cl.bgmp.commons.modules;

import cl.bgmp.butils.time.SimpleDuration;
import com.destroystokyo.paper.Title;
import java.time.Duration;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartModule extends Module {

  private Duration interval;
  private BukkitRunnable restartTask = getNewRestartTask();

  public RestartModule() {
    super(ModuleId.RESTART);
  }

  public void setInterval(Duration interval) {
    this.interval = interval;
  }

  public Duration getCurrentInterval() {
    return interval;
  }

  public void runNewRestartTask() {
    resetRestartTask();
    this.restartTask.runTaskTimer(this.commons, 0L, 20L);
  }

  public void resetRestartTask() {
    this.restartTask.cancel();
    this.restartTask = getNewRestartTask();
  }

  private BukkitRunnable getNewRestartTask() {
    return new BukkitRunnable() {
      @Override
      public void run() {
        interval = interval.minusSeconds(1);
        broadcastProgressToPlayers(Bukkit.getOnlinePlayers());
        if (interval.isZero() || interval.isNegative()) {
          sendPlayersToLobby();
          Bukkit.shutdown();
        }
      }
    };
  }

  private void broadcastProgressToPlayers(final Collection<? extends Player> players) {
    long seconds = interval.getSeconds();
    if (seconds > 60) return;
    if (seconds % 10 == 0
        || seconds == 5
        || seconds == 4
        || seconds == 3
        || seconds == 2
        || seconds == 1) {

      final int v1 = seconds <= 3 ? 20 : 10;

      players.forEach(
          player -> {
            final String secondOrSeconds =
                interval.getSeconds() == 1
                    ? this.translations.get("time.unit.second", player)
                    : this.translations.get("time.unit.seconds", player);

            final String message =
                ChatColor.AQUA
                    + this.translations.get(
                        "module.restart.restarting.in",
                        player,
                        ChatColor.DARK_RED + String.valueOf(interval.getSeconds()) + ChatColor.AQUA,
                        secondOrSeconds);

            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, v1);
            player.sendTitle(
                new Title(
                    this.translations.get("module.restart.title", player),
                    ChatColor.DARK_RED.toString()
                        + interval.getSeconds()
                        + ChatColor.AQUA
                        + " "
                        + secondOrSeconds,
                    10,
                    30,
                    15));
          });
    }
  }

  private void sendPlayersToLobby() {
    Bukkit.getOnlinePlayers()
        .forEach(
            onlinePlayer -> {
              onlinePlayer.sendMessage(
                  ChatColor.RED + "The server you were previously on is currently restarting...");
              this.commons.getBungee().sendPlayer(onlinePlayer, this.config.getLobby());
            });
  }

  @Override
  public void configure() {
    this.interval = SimpleDuration.fromString(this.config.getRestartInterval());
  }

  @Override
  public boolean isEnabled() {
    return this.config.isRestartEnabled();
  }

  @Override
  public void load() {
    if (this.isEnabled()) restartTask.runTaskTimer(this.commons, 0L, 20L);
  }

  @Override
  public void unload() {
    this.restartTask.cancel();
    setInterval(SimpleDuration.fromString(this.config.getRestartInterval()));
    resetRestartTask();
  }
}
