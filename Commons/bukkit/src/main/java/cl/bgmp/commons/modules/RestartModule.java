package cl.bgmp.commons.modules;

import cl.bgmp.butils.time.SimpleDuration;
import cl.bgmp.commons.Commons;
import com.destroystokyo.paper.Title;
import java.time.Duration;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartModule extends Module {
  private Duration interval =
      SimpleDuration.fromString(Commons.get().getConfiguration().getRestartInterval());
  private BukkitRunnable restartTask = getNewRestartTask();

  public RestartModule() {
    super(ModuleId.RESTART, Commons.get().getConfiguration().isRestartEnabled());
  }

  public void setInterval(Duration interval) {
    this.interval = interval;
  }

  public Duration getCurrentInterval() {
    return interval;
  }

  public void runNewRestartTask() {
    resetRestartTask();
    this.restartTask.runTaskTimer(Commons.get(), 0L, 20L);
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
                    ? Commons.get().getTranslations().get("time.unit.second", player)
                    : Commons.get().getTranslations().get("time.unit.seconds", player);

            final String message =
                ChatColor.AQUA
                    + Commons.get()
                        .getTranslations()
                        .get(
                            "module.restart.restarting.in",
                            player,
                            ChatColor.DARK_RED
                                + String.valueOf(interval.getSeconds())
                                + ChatColor.AQUA,
                            secondOrSeconds);

            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, v1);
            player.sendTitle(
                new Title(
                    Commons.get().getTranslations().get("module.restart.title", player),
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
              Commons.get()
                  .getBungee()
                  .sendPlayer(onlinePlayer, Commons.get().getConfiguration().getLobby());
            });
  }

  @Override
  public void load() {
    if (enabled) restartTask.runTaskTimer(Commons.get(), 0L, 20L);
  }

  @Override
  public void unload() {
    setEnabled(Commons.get().getConfiguration().isRestartEnabled());
    this.restartTask.cancel();
    setInterval(SimpleDuration.fromString(Commons.get().getConfiguration().getRestartInterval()));
    resetRestartTask();
  }
}
