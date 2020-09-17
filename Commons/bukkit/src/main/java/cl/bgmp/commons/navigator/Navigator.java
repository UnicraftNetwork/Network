package cl.bgmp.commons.navigator;

import cl.bgmp.butils.items.PlayerHeadBuilder;
import cl.bgmp.commons.Commons;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** The server navigator players are given when joining */
public class Navigator {
  public static final String playerHeadOwner = Commons.get().getConfiguration().getNavigatorHead();
  private ItemStack navigator;

  public Navigator(final Player player) {
    final String title =
        ChatColor.BLUE.toString()
            + ChatColor.BOLD
            + Commons.get().getTranslations().get("module.navigator.title", player)
            + ChatColor.RESET;
    final String[] lore =
        new String[] {
          ChatColor.GRAY
              + Commons.get().getTranslations().get("module.navigator.lore", player)
              + ChatColor.RESET
        };
    this.navigator =
        new PlayerHeadBuilder().setOwner(playerHeadOwner).setName(title).setLore(lore).build();
  }

  public ItemStack getItem() {
    return navigator;
  }
}
