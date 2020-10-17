package cl.bgmp.api;

import cl.bgmp.api.injection.APIModule;
import cl.bgmp.api.users.BukkitUserStore;
import cl.bgmp.butils.mysql.MySQLConnector;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public final class APIBukkit extends JavaPlugin {
  private static APIBukkit apiBukkit;
  private APIBukkitConfig config;
  private MySQLConnector connector;

  @Inject private BukkitUserStore userStore;

  public static APIBukkit get() {
    return apiBukkit;
  }

  public BukkitUserStore getUserStore() {
    return userStore;
  }

  public APIBukkitConfig getConfiguration() {
    return config;
  }

  public MySQLConnector getDatabase() {
    return connector;
  }

  @Override
  public void onEnable() {
    apiBukkit = this;

    this.loadConfig();

    this.connector =
        new MySQLConnector(
            APIBukkit.class,
            this.config.getMySQLHost(),
            this.config.getMySQLDatabase(),
            this.config.getMySQLUsername(),
            this.config.getMySQLPassword(),
            this.config.getMySQLPort(),
            this.config.isMySQLUsingSSL());
    this.connector.connect();

    this.inject();
  }

  private void inject() {
    final APIModule module = new APIModule(this);
    final Injector injector = module.createInjector();

    injector.injectMembers(this);
  }

  private void loadConfig() {
    this.saveDefaultConfig();
    this.reloadConfig();
    if (config != null) return;

    this.getServer().getPluginManager().disablePlugin(this);
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    try {
      this.config = new APIBukkitConfig(getConfig(), getDataFolder());
    } catch (RuntimeException e) {
      e.printStackTrace();
      this.getLogger().severe("Could not load API configuration.");
      this.getLogger().severe("Shutting down...");
    }
  }
}
