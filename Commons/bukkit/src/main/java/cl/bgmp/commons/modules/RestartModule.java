package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.timeutils.Time;
import cl.bgmp.utilsbukkit.timeutils.TimeUnit;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.destroystokyo.paper.Title;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartModule extends Module {
  private Time interval = Config.Restart.getInterval().getAs(TimeUnit.SECONDS);
  private BukkitRunnable restartTask =
      new BukkitRunnable() {
        @Override
        public void run() {
          interval = new Time(interval.getValue() - 1, TimeUnit.SECONDS);
          broadcastProgressToPlayers(Bukkit.getOnlinePlayers());
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

  public Time getCurrentInterval() {
    return interval;
  }

  public void runNewRestartTask() {
    this.restartTask.cancel();
    this.restartTask =
        new BukkitRunnable() {
          @Override
          public void run() {
            interval = new Time(interval.getValue() - 1, TimeUnit.SECONDS);
            broadcastProgressToPlayers(Bukkit.getOnlinePlayers());
            if (interval.toMinimalString().equals("0s")) {
              sendPlayersToLobby();
              Bukkit.shutdown();
            }
          }
        };
    this.restartTask.runTaskTimer(Commons.get(), 0L, Time.fromString("1s").ticks());
  }

  private void broadcastProgressToPlayers(final Collection<? extends Player> players) {
    if (interval.getValue() > 60) return;
    if (interval.getValue() % 10 == 0
        || interval.getValue() == 5
        || interval.getValue() == 4
        || interval.getValue() == 3
        || interval.getValue() == 2
        || interval.getValue() == 1) {

      final int v1 = interval.getValue() <= 3 ? 20 : 10;

      players.forEach(
          player -> {
            final String secondOrSeconds =
                interval.toMinimalString().equals("1s")
                    ? Translations.get("time.unit.second", player)
                    : Translations.get("time.unit.seconds", player);

            final String message =
                ChatColor.AQUA
                    + Translations.get(
                        "module.restart.restarting.in",
                        player,
                        ChatColor.DARK_RED + String.valueOf(interval.getValue()) + ChatColor.AQUA,
                        secondOrSeconds);

            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, v1);
            player.sendTitle(
                new Title(
                    ChatColor.AQUA + Translations.get("module.restart.title", player),
                    ChatColor.DARK_RED.toString()
                        + interval.getValue()
                        + ChatColor.AQUA
                        + " "
                        + interval.getUnit().toLocalizedString(player),
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
              Channels.sendPlayerToServer(
                  Commons.get(), onlinePlayer, Config.Lobby.getLobbyServerName());
            });
  }

  @Override
  public void load() {
    if (enabled) restartTask.runTaskTimer(Commons.get(), 0L, Time.fromString("1s").ticks());
  }

  @Override
  public void unload() {}
}
