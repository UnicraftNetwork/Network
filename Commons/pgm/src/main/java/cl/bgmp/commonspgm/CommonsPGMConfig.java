package cl.bgmp.commonspgm;

import java.io.File;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CommonsPGMConfig implements Config {
  private FileConfiguration config;
  private File datafolder;

  private boolean isNavigatorEnabled;
  private String navigatorHead;
  private String navigatorTitle;
  private int navigatorSize;
  private ConfigurationSection navigatorButtons;

  private String lobby;

  private boolean areTipsEnabled;
  private String tipsInterval;
  private String tipsPrefix;
  private List<String> tips;

  public CommonsPGMConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;

    this.isNavigatorEnabled = config.getBoolean("navigator.enabled");
    this.navigatorHead = config.getString("navigator.head");
    this.navigatorTitle = config.getString("navigator.title");
    this.navigatorSize = config.getInt("navigator.size");
    this.navigatorButtons = this.config.getConfigurationSection("navigator.servers");

    this.lobby = this.config.getString("lobby");

    this.areTipsEnabled = this.config.getBoolean("tips.enabled");
    this.tipsInterval = this.config.getString("tips.interval");
    this.tipsPrefix = this.config.getString("tips.prefix");
    this.tips = this.config.getStringList("tips.messages");
  }

  @Override
  public boolean isNavigatorEnabled() {
    return isNavigatorEnabled;
  }

  @Override
  public String getNavigatorHead() {
    return navigatorHead;
  }

  @Override
  public String getNavigatorTitle() {
    return navigatorTitle;
  }

  @Override
  public int getNavigatorSize() {
    return navigatorSize;
  }

  @Override
  public ConfigurationSection getNavigatorButtons() {
    return navigatorButtons;
  }

  @Override
  public boolean areTipsEnabled() {
    return areTipsEnabled;
  }

  @Override
  public String getTipsInterval() {
    return tipsInterval;
  }

  @Override
  public String getTipsPrefix() {
    return tipsPrefix;
  }

  @Override
  public List<String> getTips() {
    return tips;
  }

  @Override
  public String getLobby() {
    return lobby;
  }
}
