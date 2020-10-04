package cl.bgmp.commons.modules.navigator;

import cl.bgmp.butils.bungee.Server;
import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.gui.GUI;
import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigatorGUI extends GUI {
  private Player owner;
  private Commons commons;
  private Set<ServerButton> serverButtons;

  public NavigatorGUI(Player owner, Commons commons, Config config, Translations translations) {
    super(
        commons,
        Chat.color(translations.get("module.navigator.title", owner)),
        config.getNavigatorSize(),
        new ItemStack(Material.AIR));
    this.commons = commons;
    this.serverButtons = parseNavigatorButtons(config.getNavigatorButtons());
  }

  private Set<ServerButton> parseNavigatorButtons(ConfigurationSection navigatorServersSection) {
    final Set<ServerButton> buttons = new HashSet<>();

    if (navigatorServersSection != null) {
      for (String key : navigatorServersSection.getKeys(false)) {
        int slot = Integer.parseInt(key);
        String serverName = navigatorServersSection.getString(key + ".name");
        String ip = navigatorServersSection.getString(key + ".ip");
        int port = navigatorServersSection.getInt(key + ".port");

        ItemStack item = new ItemStack(Material.AIR);
        ConfigurationSection itemSection =
            navigatorServersSection.getConfigurationSection(key + ".item");
        if (itemSection != null) {
          ItemStack parsedItem = Config.parseItem(itemSection);
          item = parsedItem == null ? item : parsedItem;
        }

        buttons.add(
            new ServerButton(
                item, slot, this.commons.getBungee(), new Server(serverName, ip, port)));
      }
    }

    return buttons;
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
      serverButton.clickBy(player);
    }
  }
}
