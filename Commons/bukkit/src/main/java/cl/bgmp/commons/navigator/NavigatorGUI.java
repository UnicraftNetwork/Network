package cl.bgmp.commons.navigator;

import cl.bgmp.butils.bungee.Bungee;
import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.gui.GUI;
import cl.bgmp.commons.Commons;
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
        Chat.color(Commons.get().getConfiguration().getNavigatorTitle()),
        Commons.get().getConfiguration().getNavigatorSize(),
        new ItemStack(Material.AIR));
    this.serverButtons = Commons.get().getConfiguration().getNavigatorButtons();
    Commons.get().registerEvents(this);
  }

  @Override
  public void addContent() {
    super.addContent();
    for (ServerButton serverButton : serverButtons) {
      setItem(serverButton.getSlot(), serverButton.getItemStack());
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
      Bungee.sendPlayer(Commons.get(), player, serverButton.getServer().getName());
    }
  }
}
