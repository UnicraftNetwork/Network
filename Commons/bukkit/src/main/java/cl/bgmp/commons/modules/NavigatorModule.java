package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.commons.navigator.Navigator;
import cl.bgmp.commons.navigator.NavigatorGUI;
import cl.bgmp.utilsbukkit.translations.Translations;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class NavigatorModule extends Module {

  public NavigatorModule() {
    super(ModuleId.NAVIGATOR, Config.Navigator.isEnabled());
  }

  /**
   * Compares the given item to the navigator's item and checks if they are equal
   *
   * @param itemStack The item to compare to navigator's item
   * @return Whether or not the two items are equal
   */
  @SuppressWarnings({"ConstantConditions", "deprecation"})
  private boolean itemIsNavigator(final @NotNull ItemStack itemStack) {
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) return false;

    if (itemStack.getItemMeta() instanceof SkullMeta) {
      final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
      return skullMeta.getOwner().equals(Navigator.playerHeadOwner);
    } else return false;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final ItemStack itemInHand = event.getItem();
    if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
      return;
    if (itemInHand == null || itemInHand.getType() == Material.AIR) return;
    // FIXME: Statement bellow should never be true, and must be filtered in config rather than
    // here, I'll fix it... eventually
    if (Config.Navigator.getServerButtons().stream().anyMatch(button -> button.getItem() == null)) {
      event
          .getPlayer()
          .sendMessage(ChatColor.RED + Translations.get("misc.unknown.error", event.getPlayer()));
      return;
    }
    if (itemIsNavigator(itemInHand))
      event.getPlayer().openInventory(new NavigatorGUI().getInventory());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    final PlayerInventory inventory = event.getPlayer().getInventory();
    if (!itemIsNavigator(inventory.getItemInMainHand())
        && !itemIsNavigator(inventory.getItemInOffHand())) return;
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().getInventory().setItem(4, new Navigator(event.getPlayer()).getItem());
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {
    setEnabled(Config.Navigator.isEnabled());
    Commons.get().unregisterEvents(this);
  }
}
