package cl.bgmp.commons.Navigator;

import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Items.PlayerHeads;
import org.bukkit.inventory.ItemStack;

/** The server navigator players are given when joining */
public class Navigator {
  private String title = Chat.colourify("&9&lNavigator&r");
  private String[] lore = new String[] {Chat.colourify("&7Right-Click to pick a Server!&r")};
  private ItemStack navigator = PlayerHeads.titledPlayerHeadWithLore("Skyron_Varn", title, lore);

  // Note: if navigator's item happens to be a player head, it must contain a lore for validation matters!
  public Navigator() {
  }

  public ItemStack getItem() {
    return navigator;
  }
}
