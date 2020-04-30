package cl.bgmp.commons.Navigator;

import cl.bgmp.utilsbukkit.Items.PlayerHeads;
import cl.bgmp.utilsbukkit.Translations.Translations;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** The server navigator players are given when joining */
public class Navigator {
  public static final String playerHeadOwner = "Skyron_Varn";
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
