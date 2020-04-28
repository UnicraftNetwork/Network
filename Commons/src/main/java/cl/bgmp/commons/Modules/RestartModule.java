package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.TimeUtils.Time;
import cl.bgmp.utilsbukkit.TimeUtils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

// TODO: Translations
public class RestartModule extends Module {
  private Time interval = Config.Restart.getInterval().getAs(TimeUnit.SECONDS);
  private BukkitRunnable restartTask =
      new BukkitRunnable() {
        @Override
        public void run() {
          interval = new Time(interval.getValue() - 1, TimeUnit.SECONDS);
          broadcastProgress();
          if (interval.toMinimalString().equals("0s")) {
            sendPlayersToLobby();
            Bukkit.shutdown();
          }
        }
      };

  public RestartModule() {
    super(ModuleId.RESTART, Config.Restart.isEnabled());
  }

  public void setInterval(Time interval) {
    this.interval = interval.getAs(TimeUnit.SECONDS);
  }

  public void runNewRestartTask() {
    this.restartTask.cancel();
    this.restartTask =
        new BukkitRunnable() {
          @Override
          public void run() {
            interval = new Time(interval.getValue() - 1, TimeUnit.SECONDS);
            broadcastProgress();
            if (interval.toMinimalString().equals("0s")) {
              sendPlayersToLobby();
              Bukkit.shutdown();
            }
          }
        };
    this.restartTask.runTaskTimer(Commons.get(), 0L, Time.fromString("1s").ticks());
  }

  private void broadcastProgress() {
    if (interval.getValue() > 60) return;
    if (interval.getValue() % 10 == 0
        || interval.getValue() == 5
        || interval.getValue() == 4
        || interval.getValue() == 3
        || interval.getValue() == 2
        || interval.getValue() == 1) {
      String secondOrSeconds = interval.toMinimalString().equals("1s") ? "second" : "seconds";
      Bukkit.broadcastMessage(
          ChatColor.AQUA
              + "Server is restarting in "
              + ChatColor.DARK_RED
              + interval.getValue()
              + ChatColor.AQUA
              + " "
              + secondOrSeconds);
    }
  }

  private void sendPlayersToLobby() {
    Bukkit.getOnlinePlayers()
        .forEach(
            onlinePlayer -> {
              onlinePlayer.sendMessage(
                  ChatColor.RED + "The server you were previously on is currently restarting...");
              Channels.sendPlayerToServer(
                  Commons.get(), onlinePlayer, Config.Lobby.getLobbyServerName());
            });
  }

  @Override
  public void load() {
    if (enabled) {
      restartTask.runTaskTimer(Commons.get(), 0L, Time.fromString("1s").ticks());
      // Commons.get().registerEvents(this);
    }
  }

  @Override
  public void unload() {}
}
