package cl.bgmp.lobbyx;

import org.bukkit.Location;

public interface Config {

  Location getSpawn();

  float getWalkSpeed();

  float getFlySpeed();

  String getBypassPerm();
}
