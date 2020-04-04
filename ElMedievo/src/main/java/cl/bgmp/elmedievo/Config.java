package cl.bgmp.elmedievo;

import cl.bgmp.utilsbukkit.Validate;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
  private static Logger logger = ElMedievo.get().getLogger();

  private static Configuration getConfig() {
    ElMedievo lobby = ElMedievo.get();
    if (lobby != null) return lobby.getConfig();
    else return new YamlConfiguration();
  }

  public static class Spawn {
    private static final String spawnPath = "spawn";
    private static final String worldPath = spawnPath + ".world";
    private static final String pointPath = spawnPath + ".point";
    private static final String yawPath = spawnPath + ".yaw";
    private static final String pitchPath = spawnPath + ".pitch";

    public static World getWorld() {
      if (!Validate.pathsAreValid(getConfig(), worldPath)) return Bukkit.getWorlds().get(0);
      else {
        final String worldString = getConfig().getString(worldPath);
        assert worldString != null;
        return Bukkit.getWorld(worldString);
      }
    }

    public static Location getLocation() {
      final ConfigurationSection spawn = getConfig().getConfigurationSection(spawnPath);
      if (spawn != null && Validate.pathsAreValid(spawn, pointPath, yawPath, pitchPath)) {
        final String point = getConfig().getString(pointPath);
        final String yawString = getConfig().getString(yawPath);
        final String pitchString = getConfig().getString(pitchPath);
        assert point != null && yawString != null && pitchString != null;

        final String[] coords = point.split(",");

        return new Location(
            getWorld(),
            Double.parseDouble(coords[0]),
            Double.parseDouble(coords[1]),
            Double.parseDouble(coords[2]),
            Float.parseFloat(yawString),
            Float.parseFloat(pitchString));
      } else {
        logger.severe("Spawn could not be parsed. Please check your config.yml file.");
        logger.severe("A default spawn has been set instead. Location: 0, 100, 0");
        return new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
      }
    }
  }
}
