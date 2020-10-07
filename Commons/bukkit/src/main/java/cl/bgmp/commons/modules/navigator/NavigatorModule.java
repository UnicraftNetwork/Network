package cl.bgmp.commons.modules.navigator;

import cl.bgmp.butils.items.PlayerHeadBuilder;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;

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
  private boolean itemIsNavigator(final @Nonnull ItemStack itemStack) {
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
    if (!itemIsNavigator(inventory.getItemInMainHand())
        && !itemIsNavigator(inventory.getItemInOffHand())) return;
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    ItemStack navigatorItem =
        new PlayerHeadBuilder()
            .setOwner(this.config.getNavigatorHead())
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
