package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.items.Items;
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
              Items.titledItemStackWithLore(
                  Material.COMPASS,
                  Chat.colourify("&9&lTeleport Tool&r"),
                  new String[] {Chat.colourify("&7Click to teleport!&r")}));
          add(Items.titledItemStack(Material.RABBIT_FOOT, Chat.colourify("&5&lEdit Wand")));
        }
      };

  public JoinToolsModule() {
    super(ModuleId.JOIN_TOOLS, Config.Tools.areEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    player.getInventory().clear();

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
    setEnabled(Config.Tools.areEnabled());
    Commons.get().unregisterEvents(this);
  }
}
