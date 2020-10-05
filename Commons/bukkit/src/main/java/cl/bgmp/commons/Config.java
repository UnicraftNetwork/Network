package cl.bgmp.commons;

import cl.bgmp.butils.items.ItemBuilder;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface Config {

  /** Join Tools */
  boolean areOnJoinToolsEnabled();

  /** Navigator */
  boolean isNavigatorEnabled();

  String getNavigatorHead();

  String getNavigatorTitle();

  int getNavigatorSize();

  ConfigurationSection getNavigatorButtons();

  /** VaultAPI Formatting */
  boolean isVaultFormattingEnabled();

  String getVaultFormat();

  /** Force Gamemode */
  boolean isForceGamemodeEnabled();

  String getForceGamemodeExemptPerm();

  GameMode getForcedGamemode();

  /** JoinQuitMessages */
  boolean areJoinQuitMessagesEnabled();

  boolean areJoinQuitMessagesSuppressed();

  String getJoinMessage();

  String getQuitMessage();

  /** Weather (Rain) */
  boolean isWeatherDisabled();

  /** Lobby server name */
  String getLobby();

  /** Restart */
  boolean isRestartEnabled();

  String getRestartInterval();

  /** Tips */
  boolean areTipsEnabled();

  String getTipsInterval();

  String getTipsPrefix();

  List<String> getTips();

  /** Chat censor */
  boolean isChatCensored();

  String getChatCensorExemptPermission();

  static ItemStack parseItem(ConfigurationSection section) {
    String material = section.getString("material");
    String title = section.getString("title");
    String[] lore = section.getStringList("lore").toArray(new String[0]);
    if (material == null || title == null || lore == null) return null;

    return new ItemBuilder(Material.valueOf(material)).setName(title).setLore(lore).build();
  }
}
