package cl.bgmp.commons.Navigator;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Items.PlayerHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/** The server navigator players are given when joining */
public class Navigator implements Listener {
  private String title = Chat.colourify("&9&lNavigator&r");
  private String[] lore = new String[] {Chat.colourify("&7Right-Click to pick a Server!&r")};

  // Note: if navigator happens to be a player head, it must contain a lore for validation matters!
  private ItemStack navigator = PlayerHeads.titledPlayerHeadWithLore("Skyron_Varn", title, lore);

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

    boolean itemInHandIsNavigator = false;

    if (navigator.getItemMeta() instanceof SkullMeta) {
      if (!itemInHand.hasItemMeta()
              || !itemInHand.getItemMeta().hasLore()
              || !navigator.getItemMeta().hasLore()
      ) return;

      if (itemInHand.getLore().equals(navigator.getLore())) itemInHandIsNavigator = true;
    } else if (itemInHand.equals(navigator)) itemInHandIsNavigator = true;

    if (itemInHandIsNavigator) event.getPlayer().openInventory(new NavigatorGUI().getInventory());
  }
}
