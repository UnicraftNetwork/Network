package cl.bgmp.commons.modules;

import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.commons.Commons;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinToolsModule extends Module {

  // TODO: Probably not the best of practices to keep this here
  private static List<ItemStack> onJoinTools =
      new ArrayList<ItemStack>() {
        {
          add(
              new ItemBuilder(Material.COMPASS)
                  .setName("&9&lTeleport Tool&r")
                  .setLore("&7Click to teleport!&r")
                  .build());
          add(new ItemBuilder(Material.RABBIT_FOOT).setName("&5&lEdit Wand").build());
        }
      };

  public JoinToolsModule() {
    super(ModuleId.JOIN_TOOLS, Commons.get().getConfiguration().areOnJoinToolsEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    if (!player.hasPermission("commons.tools")) return;
    player.getInventory().setItem(0, onJoinTools.get(0));
    player.getInventory().setItem(1, onJoinTools.get(1));
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {
    setEnabled(Commons.get().getConfiguration().areOnJoinToolsEnabled());
    Commons.get().unregisterEvents(this);
  }
}
