package cl.bgmp.utilsbukkit;

import org.bukkit.configuration.ConfigurationSection;

public interface Validate {
  static boolean anyAreNull(Object... objects) {
    for (Object object : objects) if (object == null) return true;
    return false;
  }

  static boolean pathsAreValid(ConfigurationSection configurationSection, String... paths) {
    for (String path : paths) {
      try {
        configurationSection.get(path);
      } catch (NullPointerException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
}
