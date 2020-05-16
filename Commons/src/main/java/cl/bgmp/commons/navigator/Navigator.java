package cl.bgmp.commons.navigator;

import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.items.PlayerHeads;
import cl.bgmp.utilsbukkit.translations.Translations;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** The server navigator players are given when joining */
public class Navigator {
  public static final String playerHeadOwner = Config.Navigator.getNavigatorHead();
  private ItemStack navigator;

  public Navigator(final Player player) {
    final String title =
        ChatColor.BLUE.toString()
            + ChatColor.BOLD
            + Translations.get("module.navigator.title", player)
            + ChatColor.RESET;
    final String[] lore =
        new String[] {
          ChatColor.GRAY + Translations.get("module.navigator.lore", player) + ChatColor.RESET
        };
    this.navigator = PlayerHeads.titledPlayerHeadWithLore(playerHeadOwner, title, lore);
  }

  public ItemStack getItem() {
    return navigator;
  }
}
