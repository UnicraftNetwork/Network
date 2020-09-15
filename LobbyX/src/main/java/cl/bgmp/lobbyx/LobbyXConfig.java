package cl.bgmp.lobbyx;

import cl.bgmp.butils.locations.Locations;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class LobbyXConfig implements Config {
  private FileConfiguration config;
  private File datafolder;

  private Location spawn;
  private float walkSpeed;
  private float flySpeed;
  private String bypassPerm;

  @SuppressWarnings("ConstantConditions")
  public LobbyXConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;
    this.spawn = Locations.fromString(config.getString("spawn"), ",");
    this.walkSpeed = Float.parseFloat(config.getString("walk-speed"));
    this.flySpeed = Float.parseFloat(config.getString("fly-speed"));
    this.bypassPerm = config.getString("bypass-permission");
  }

  @Override
  public Location getSpawn() {
    return spawn;
  }

  @Override
  public float getWalkSpeed() {
    return walkSpeed;
  }

  @Override
  public float getFlySpeed() {
    return flySpeed;
  }

  @Override
  public String getBypassPerm() {
    return bypassPerm;
  }
}
