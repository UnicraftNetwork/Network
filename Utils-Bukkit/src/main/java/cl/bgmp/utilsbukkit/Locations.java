package cl.bgmp.utilsbukkit;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface Locations {
  static String formatLocationAsSplittable(Location location, String regex) {
    return location.getWorld().getName()
        + regex
        + location.getX()
        + regex
        + location.getY()
        + regex
        + location.getZ()
        + regex
        + location.getYaw()
        + regex
        + location.getPitch();
  }

  static Location parseLocationFromSplit(String[] split, boolean precise) {
    try {
      if (precise) {
        return new Location(
            Bukkit.getWorld(split[0]),
            Double.parseDouble(split[1]),
            Double.parseDouble(split[2]),
            Double.parseDouble(split[3]),
            Float.parseFloat(split[4]),
            Float.parseFloat(split[5]));
      } else {
        return new Location(
            Bukkit.getWorld(split[0]),
            Double.parseDouble(split[1]),
            Double.parseDouble(split[2]),
            Double.parseDouble(split[3]));
      }

    } catch (NullPointerException | NumberFormatException e) {
      e.printStackTrace();
    }
    return null;
  }

  static Location getOffsetLocation(Location location, int offset) {
    final int x = new Random().nextInt(2 * offset + 1) - offset;
    final int z = new Random().nextInt(2 * offset + 1) - offset;

    return location.add(new Vector(x, 0, z));
  }

  static float mirrorYaw(float yaw) {
    yaw = Math.abs(yaw);
    if (yawIsInRange(yaw, 45f, 135f)) return 90f;
    if (yawIsInRange(yaw, 135f, 225f)) return 0f;
    if (yawIsInRange(yaw, 225f, 315f)) return -90f;
    else return 180f;
  }

  static boolean yawIsInRange(float yaw, float min, float max) {
    return yaw >= min && yaw <= max;
  }
}
