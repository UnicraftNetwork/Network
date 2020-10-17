package cl.bgmp.commons.modules;

import cl.bgmp.butils.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinToolsModule extends Module {

  public JoinToolsModule() {
    super(ModuleId.JOIN_TOOLS);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    ItemStack compass =
        new ItemBuilder(Material.COMPASS)
            .setName(
                ChatColor.BLUE.toString()
                    + ChatColor.BOLD
                    + this.translations.get("module.jointools.compass.title", player))
            .setLore(
                ChatColor.GRAY + this.translations.get("module.jointools.compass.lore", player))
            .build();
    ItemStack wand =
        new ItemBuilder(Material.RABBIT_FOOT)
            .setName(
                ChatColor.DARK_PURPLE.toString()
                    + ChatColor.BOLD
                    + this.translations.get("module.jointools.wand.title", player))
            .build();

    if (!player.hasPermission("commons.tools")) return;

    player.getInventory().setItem(0, compass);
    player.getInventory().setItem(1, wand);
  }

  @Override
  public boolean isEnabled() {
    return this.config.areOnJoinToolsEnabled();
  }
}
