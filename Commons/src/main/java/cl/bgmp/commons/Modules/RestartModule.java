package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.TimeUtils.Time;
import cl.bgmp.utilsbukkit.TimeUtils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartModule extends Module {
  private Time interval = Config.Restart.getInterval();
  private Time finalCountdown = Config.Restart.getFinalCountdown().getAs(TimeUnit.SECONDS);

  public RestartModule() {
    super(ModuleId.RESTART, Config.Restart.isEnabled());

    new BukkitRunnable() {
      @Override
      public void run() {
        restartServer();
      }
    }.runTaskLater(Commons.get(), interval.ticks());
  }

  // TODO: Translations
  private void restartServer() {
    new BukkitRunnable() {
      @Override
      public void run() {
        finalCountdown = new Time(finalCountdown.getValue() - 1, TimeUnit.SECONDS);

        if (finalCountdown.getValue() % 10 == 0
            || finalCountdown.getValue() == 5
            || finalCountdown.getValue() == 4
            || finalCountdown.getValue() == 3
            || finalCountdown.getValue() == 2
            || finalCountdown.getValue() == 1) {
          String secondOrSeconds =
              finalCountdown.toMinimalString().equals("1s") ? "second" : "seconds";
          Bukkit.broadcastMessage(
              ChatColor.AQUA
                  + "Server is restarting in "
                  + ChatColor.DARK_RED
                  + finalCountdown.getValue()
                  + ChatColor.AQUA
                  + " "
                  + secondOrSeconds);
        }

        if (finalCountdown.toMinimalString().equals("0s")) {
          Bukkit.getOnlinePlayers()
              .forEach(
                  onlinePlayer -> {
                    onlinePlayer.sendMessage(
                        ChatColor.RED
                            + "The server you were previously on is currently restarting...");
                    Channels.sendPlayerToServer(
                        Commons.get(), onlinePlayer, Config.Lobby.getLobbyServerName());
                  });

          Bukkit.shutdown();
          this.cancel();
        }
      }
    }.runTaskTimer(Commons.get(), 0L, Time.fromString("1s").ticks());
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {}
}
