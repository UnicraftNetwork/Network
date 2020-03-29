package cl.bgmp.commons;

import cl.bgmp.commons.Navigator.Navigator;
import cl.bgmp.utilsbukkit.Channels;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Commons extends JavaPlugin {
  private static Commons commons;
  private Navigator navigator;

  public static Commons get() {
    return commons;
  }

  @Override
  public void onEnable() {
    commons = this;
    loadConfiguration();

    Channels.registerBungeeToPlugin(this);
    navigator = new Navigator();

    registerEvents();
  }

  @Override
  public void onDisable() {}

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
