package cl.bgmp.elmedievo;

import cl.bgmp.utilsbukkit.timeutils.Time;
import cl.bgmp.utilsbukkit.timeutils.TimeUnit;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config {
  private static Logger logger = ElMedievo.get().getLogger();

  private static Configuration getConfig() {
    ElMedievo lobby = ElMedievo.get();
    if (lobby != null) return lobby.getConfig();
    else return new YamlConfiguration();
  }

  public static void save() {
    ElMedievo.get().saveConfig();
  }

  public static void reload() {
    ElMedievo.get().reloadConfig();
  }

  public static class Event {
    public static boolean inInitialPhase() {
      return getConfig().getBoolean("event.initial-phase");
    }

    public static Time getGracePeriod() {
      String timeString = getConfig().getString("event.grace");
      return timeString == null ? new Time(15, TimeUnit.MINUTES) : Time.fromString(timeString);
    }

    public static List<String> getParticipants() {
      return getConfig().getStringList("event.participants");
    }

    public static void addParticipant(Player player) {
      List<String> participants = getParticipants();
      if (participants.contains(player.getName())) return;

      participants.add(player.getName());
      getConfig().set("event.participants", participants);

      player.sendMessage(
          ChatColor.GREEN
              + "=> Estás automáticamente registrado para el evento de destrucción de Towny.");
      player.sendMessage(
          ChatColor.GREEN
              + "=> Recuerda que el evento comienza a las 21:00! Tienes hasta las 21:15 para entrar luego del reinicio!");
      save();
    }
  }
}
