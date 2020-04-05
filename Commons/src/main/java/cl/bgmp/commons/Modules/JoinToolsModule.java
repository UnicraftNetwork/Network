package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Items.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JoinToolsModule extends Module {
    public static final String id = "join-module";

    private static List<ItemStack> onJoinTools = new ArrayList<ItemStack>() {{
        add(Items.titledItemStackWithLore(Material.COMPASS, Chat.colourify("&9&lTeleport Tool&r"), new String[]{Chat.colourify("&7Click to teleport!&r")}));
        add(Items.titledItemStack(Material.RABBIT_FOOT, Chat.colourify("&5&lEdit Wand")));
    }};

    public JoinToolsModule() {
        super(id);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.getInventory().clear();

        final Module module = Commons.get().getModule(NavigatorModule.id);
        if (module != null) {
            final NavigatorModule navigatorModule = (NavigatorModule) module;
            if (navigatorModule.isEnabled()) player.getInventory().setItem(4, navigatorModule.getNavigator().getItem());
        }

        if (!player.hasPermission("commons.tools")) return;
        player.getInventory().setItem(0, onJoinTools.get(0));
        player.getInventory().setItem(1, onJoinTools.get(1));
    }

    @Override
    public void load() {
        this.enabled = Config.Tools.areEnabled();
        if (enabled) Commons.get().registerEvents(this);
    }

    @Override
    public void unload() {
    }
}
