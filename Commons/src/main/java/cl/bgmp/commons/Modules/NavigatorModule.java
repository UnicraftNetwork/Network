package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.commons.Navigator.Navigator;
import cl.bgmp.commons.Navigator.NavigatorGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class NavigatorModule extends Module {
  private Navigator navigator = new Navigator();

  public NavigatorModule() {
    super(ModuleId.NAVIGATOR, Config.Navigator.isEnabled());
  }

  public Navigator getNavigator() {
    return navigator;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final ItemStack itemInHand = event.getItem();
    if (itemInHand == null || itemInHand.getType() == Material.AIR) return;

    final ItemStack navigatorItem = navigator.getItem();
    boolean itemInHandIsNavigator = false;

    if (navigatorItem.getItemMeta() instanceof SkullMeta) {
      if (!itemInHand.hasItemMeta()
          || !itemInHand.getItemMeta().hasLore()
          || !navigatorItem.getItemMeta().hasLore()) return;

      if (itemInHand.getLore().equals(navigatorItem.getLore())) itemInHandIsNavigator = true;
    } else if (itemInHand.equals(navigator)) itemInHandIsNavigator = true;

    if (itemInHandIsNavigator) event.getPlayer().openInventory(new NavigatorGUI().getInventory());
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {}
}
