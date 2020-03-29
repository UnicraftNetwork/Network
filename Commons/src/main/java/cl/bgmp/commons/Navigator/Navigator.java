package cl.bgmp.commons.Navigator;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/** The server navigator players are given when joining */
public class Navigator implements Listener {
  private String title = Chat.colourify("&9&lNavigator");
  private String[] lore = new String[] {Chat.colourify("&7Right-Click to pick a Server!")};
  private ItemStack navigator = Items.titledItemStackWithLore(Material.COMPASS, title, lore);

  public Navigator() {
    if (Config.Navigator.isEnabled()) {
      Commons.get().registerEvents(this);
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    player.getInventory().clear();
    player.getInventory().setItem(4, navigator);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final ItemStack itemInHand = event.getItem();
    if (itemInHand == null || itemInHand.getType() == Material.AIR) return;

    if (itemInHand.equals(navigator)) {
      final Player player = event.getPlayer();
      player.openInventory(new NavigatorGUI().getInventory());
    }
  }
}
