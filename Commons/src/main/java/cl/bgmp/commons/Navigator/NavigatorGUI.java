package cl.bgmp.commons.Navigator;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.GUI;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigatorGUI extends GUI implements Listener {
  private Set<ServerButton> serverButtons;

  public NavigatorGUI() {
    super(
        Commons.get(),
        Chat.colourify(Config.Navigator.getTitle()),
        Config.Navigator.getSize(),
        new ItemStack(Material.AIR));
    this.serverButtons = Config.Navigator.getServerButtons();
    Commons.get().registerEvents(this);
  }

  @Override
  public void addContent() {
    super.addContent();
    for (ServerButton serverButton : serverButtons) {
      setItem(serverButton.getSlot(), serverButton.getItem());
    }
  }

  @Override
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!event.getInventory().equals(getInventory())) return;
    event.setCancelled(true);

    final Inventory clickedInventory = event.getClickedInventory();
    if (clickedInventory == null || !clickedInventory.equals(getInventory())) return;

    final HumanEntity humanEntity = event.getWhoClicked();
    if (!(humanEntity instanceof Player)) return;

    final Player player = (Player) event.getWhoClicked();
    final int slot = event.getSlot();

    for (ServerButton serverButton : serverButtons) {
      if (slot != serverButton.getSlot()) continue;
      player.sendMessage(
          ChatColor.DARK_PURPLE
              + "Sending you to "
              + ChatColor.WHITE
              + "["
              + ChatColor.GOLD
              + serverButton.getServer().getName()
              + ChatColor.WHITE
              + "]");
      Channels.sendPlayerToServer(Commons.get(), player, serverButton.getServer().getName());
    }
  }
}
