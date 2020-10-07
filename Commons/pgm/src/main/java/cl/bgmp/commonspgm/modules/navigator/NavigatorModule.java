package cl.bgmp.commonspgm.modules.navigator;

import cl.bgmp.commonspgm.modules.Module;
import cl.bgmp.commonspgm.modules.ModuleId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import tc.oc.pgm.spawns.events.ObserverKitApplyEvent;

public class NavigatorModule extends Module {

  public NavigatorModule() {
    super(ModuleId.NAVIGATOR);
  }

  /**
   * Compares the given item to the navigator's item and checks if they are equal
   *
   * @param itemStack The item to compare to navigator's item
   * @return Whether or not the two items are equal
   */
  private boolean itemIsNavigator(final ItemStack itemStack) {
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) return false;

    if (itemStack.getItemMeta() instanceof SkullMeta) {
      final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
      if (skullMeta.getOwner() == null) return false;
      return skullMeta.getOwner().equals(this.config.getNavigatorHead());
    } else return false;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final Action action = event.getAction();
    if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) return;

    final ItemStack itemInHand = event.getItem();
    if (itemInHand == null || itemInHand.getType() == Material.AIR) return;
    if (!itemIsNavigator(itemInHand)) return;

    event
        .getPlayer()
        .openInventory(
            new NavigatorGUI(event.getPlayer(), this.commons, this.config, this.translations)
                .getInventory());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    final PlayerInventory inventory = event.getPlayer().getInventory();
    if (!itemIsNavigator(inventory.getItemInHand())) return;
    event.setCancelled(true);
  }

  @EventHandler
  public void onObserverKitApply(final ObserverKitApplyEvent event) {
    final Player player = event.getPlayer().getBukkit();

    final ItemStack navigatorItem =
        new LegacyPlayerHeadBuilder(this.config.getNavigatorHead())
            .setName(
                ChatColor.BLUE.toString()
                    + ChatColor.BOLD
                    + this.translations.get("module.navigator.title", player))
            .setLore(ChatColor.GRAY + this.translations.get("module.navigator.lore", player))
            .build();

    event.getPlayer().getInventory().setItem(4, navigatorItem);
  }

  @Override
  public boolean isEnabled() {
    return this.config.isNavigatorEnabled();
  }
}
