package cl.bgmp.commons;

import cl.bgmp.butils.bungee.Bungee;
import cl.bgmp.butils.bungee.Server;
import cl.bgmp.commons.navigator.ServerButton;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommonsConfig implements Config {
  private FileConfiguration config;
  private File datafolder;

  private boolean areOnJoinToolsEnabled;

  private boolean isNavigatorEnabled;
  private String navigatorHead;
  private String navigatorTitle;
  private int navigatorSize;
  private Set<ServerButton> navigatorButtons;

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

  public CommonsConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;

    this.areOnJoinToolsEnabled = config.getBoolean("onjoin-tools");

    this.isNavigatorEnabled = config.getBoolean("navigator.enabled");
    this.navigatorHead = config.getString("navigator.head");
    this.navigatorTitle = config.getString("navigator.title");
    this.navigatorSize = config.getInt("navigator.size");
    this.navigatorButtons =
        this.parseNavigatorButtons(this.config.getConfigurationSection("navigator.servers"));

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
  }

  private Set<ServerButton> parseNavigatorButtons(ConfigurationSection navigatorServersSection) {
    final Set<ServerButton> buttons = new HashSet<>();

    if (navigatorServersSection != null) {
      for (String key : navigatorServersSection.getKeys(false)) {
        int slot = Integer.parseInt(key);
        String serverName = navigatorServersSection.getString(key + ".name");
        String ip = navigatorServersSection.getString(key + ".ip");
        int port = navigatorServersSection.getInt(key + ".port");

        ItemStack item = new ItemStack(Material.AIR);
        ConfigurationSection itemSection =
            navigatorServersSection.getConfigurationSection(key + ".item");
        if (itemSection != null) {
          ItemStack parsedItem = Config.parseItem(itemSection);
          item = parsedItem == null ? item : parsedItem;
        }

        buttons.add(
            new ServerButton(item, slot, new Server(serverName, ip, port)) {
              @Override
              public void clickBy(Player player) {
                Bungee.sendPlayer(Commons.get(), player, serverName);
              }
            });
      }
    }

    return buttons;
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
  public Set<ServerButton> getNavigatorButtons() {
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
}
