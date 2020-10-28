package cl.bgmp.commonspgm;

import cl.bgmp.butils.items.ItemBuilder;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface Config {

  /** Navigator */
  boolean isNavigatorEnabled();

  String getNavigatorHead();

  String getNavigatorTitle();

  int getNavigatorSize();

  ConfigurationSection getNavigatorButtons();

  /** Tips */
  boolean areTipsEnabled();

  String getTipsInterval();

  String getTipsPrefix();

  List<String> getTips();

  /** Lobby server name */
  String getLobby();

  static ItemStack parseItem(ConfigurationSection section) {
    String material = section.getString("material");
    String title = section.getString("title");
    String[] lore = section.getStringList("lore").toArray(new String[0]);
    if (material == null || title == null) return null;

    return new ItemBuilder(Material.valueOf(material)).setName(title).setLore(lore).build();
  }
}
