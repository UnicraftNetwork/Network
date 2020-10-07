package cl.bgmp.commonspgm.modules;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commonspgm.CommonsPGM;
import cl.bgmp.commonspgm.Config;
import com.google.inject.Inject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public abstract class Module implements Listener {
  @Inject protected CommonsPGM commons;
  @Inject protected Config config;
  @Inject protected Translations translations;

  protected ModuleId id;

  public Module(ModuleId id) {
    this.id = id;
  }

  public ModuleId getId() {
    return id;
  }

  public void configure() {}

  public abstract boolean isEnabled();

  public void load() {
    if (this.isEnabled()) {
      PluginManager pm = this.commons.getServer().getPluginManager();
      pm.registerEvents(this, this.commons);
    }
  }

  public void unload() {
    HandlerList.unregisterAll(this);
  }

  public void reload() {
    this.unload();
    this.load();
  }
}
