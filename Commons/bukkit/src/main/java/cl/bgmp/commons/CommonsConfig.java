package cl.bgmp.commons;

import java.io.File;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CommonsConfig implements Config {
  private FileConfiguration config;
  private File datafolder;

  private boolean areOnJoinToolsEnabled;

  private boolean isNavigatorEnabled;
  private String navigatorHead;
  private String navigatorTitle;
  private int navigatorSize;
  private ConfigurationSection navigatorButtons;

  private boolean isVaultFormattingEnabled;
  private String vaultFormat;

  private boolean isForceGamemodeEnabled;
  private String forceGamemodeExemptPermission;
  private GameMode forcedGamemode;

  private boolean areJoinQuitMessagesEnabled;
  private boolean areJoinQuitMessagesSuppressed;
  private String joinMessage;
  private String quitMessage;

  private boolean isWeatherDisabled;

  private String lobby;

  private boolean isRestartEnabled;
  private String restartInterval;

  private boolean areTipsEnabled;
  private String tipsInterval;
  private String tipsPrefix;
  private List<String> tips;

  private boolean isChatCensored;
  private String censorExemptPermission;

  public CommonsConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;

    this.areOnJoinToolsEnabled = config.getBoolean("onjoin-tools");

    this.isNavigatorEnabled = config.getBoolean("navigator.enabled");
    this.navigatorHead = config.getString("navigator.head");
    this.navigatorTitle = config.getString("navigator.title");
    this.navigatorSize = config.getInt("navigator.size");
    this.navigatorButtons = this.config.getConfigurationSection("navigator.servers");

    this.isVaultFormattingEnabled = this.config.getBoolean("chat.vault-formatting.enabled");
    this.vaultFormat = this.config.getString("chat.vault-formatting.format");

    this.isForceGamemodeEnabled = this.config.getBoolean("force-gamemode.enabled");
    this.forceGamemodeExemptPermission = this.config.getString("force-gamemode.exempt-permission");
    this.forcedGamemode =
        GameMode.valueOf(this.config.getString("force-gamemode.gamemode").toUpperCase());

    this.areJoinQuitMessagesEnabled = this.config.getBoolean("joinquit-messages.enabled");
    this.areJoinQuitMessagesSuppressed = this.config.getBoolean("joinquit-messages.suppress");
    this.joinMessage = this.config.getString("joinquit-messages.join");
    this.quitMessage = this.config.getString("joinquit-messages.quit");

    this.isWeatherDisabled = this.config.getBoolean("weather.disabled");

    this.lobby = this.config.getString("lobby");

    this.isRestartEnabled = this.config.getBoolean("restart.enabled");
    this.restartInterval = this.config.getString("restart.interval");

    this.areTipsEnabled = this.config.getBoolean("tips.enabled");
    this.tipsInterval = this.config.getString("tips.interval");
    this.tipsPrefix = this.config.getString("tips.prefix");
    this.tips = this.config.getStringList("tips.messages");

    this.isChatCensored = this.config.getBoolean("chat.censored.enabled");
    this.censorExemptPermission = this.config.getString("chat.censored.exempt-permission");
  }

  @Override
  public boolean areOnJoinToolsEnabled() {
    return areOnJoinToolsEnabled;
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
  public boolean isVaultFormattingEnabled() {
    return isVaultFormattingEnabled;
  }

  @Override
  public String getVaultFormat() {
    return vaultFormat;
  }

  @Override
  public boolean isForceGamemodeEnabled() {
    return isForceGamemodeEnabled;
  }

  @Override
  public String getForceGamemodeExemptPerm() {
    return forceGamemodeExemptPermission;
  }

  @Override
  public GameMode getForcedGamemode() {
    return forcedGamemode;
  }

  @Override
  public boolean areJoinQuitMessagesEnabled() {
    return areJoinQuitMessagesEnabled;
  }

  @Override
  public boolean areJoinQuitMessagesSuppressed() {
    return areJoinQuitMessagesSuppressed;
  }

  @Override
  public String getJoinMessage() {
    return joinMessage;
  }

  @Override
  public String getQuitMessage() {
    return quitMessage;
  }

  @Override
  public boolean isWeatherDisabled() {
    return isWeatherDisabled;
  }

  @Override
  public String getLobby() {
    return lobby;
  }

  @Override
  public boolean isRestartEnabled() {
    return isRestartEnabled;
  }

  @Override
  public String getRestartInterval() {
    return restartInterval;
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
  public boolean isChatCensored() {
    return isChatCensored;
  }

  @Override
  public String getChatCensorExemptPermission() {
    return censorExemptPermission;
  }

  @Override
  public boolean isPlayerServerSwitchAnnouncerEnabled() {
    return false;
  }
}
