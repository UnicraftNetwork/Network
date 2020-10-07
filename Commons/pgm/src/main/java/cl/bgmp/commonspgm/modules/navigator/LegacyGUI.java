package cl.bgmp.commonspgm.modules.navigator;

import cl.bgmp.butils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Represents a GUI. Extend this class from other classes you want to represent a GUI, and Override
 * the methods you deem necessary
 */
public abstract class LegacyGUI implements Listener {
  private final long REFRESH = 5L;

  protected Inventory inventory;
  protected String title;
  protected ItemStack background =
      new ItemBuilder(Material.STAINED_GLASS_PANE).setDamage((short) 15).build();

  /**
   * GUI
   *
   * @param plugin The plugin
   * @param title GUI's title
   * @param size GUI's size (Amount of 8 slots rows)
   */
  public LegacyGUI(Plugin plugin, String title, int size) {
    this.title = title;
    this.inventory = Bukkit.createInventory(null, size, title);

    Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::addContent, 0L, 5L);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  /**
   * GUI
   *
   * @param plugin The plugin
   * @param title GUI's title
   * @param size GUI's size (Amount of 8 slots rows)
   * @param background GUI's background item to fill empty slots
   */
  public LegacyGUI(Plugin plugin, String title, int size, ItemStack background) {
    this.title = title;
    this.inventory = Bukkit.createInventory(null, size, title);
    this.background = background;

    Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::addContent, 0L, REFRESH);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  public Inventory getInventory() {
    return inventory;
  }

  public String getTitle() {
    return title;
  }

  public void addContent() {
    if (inventory.getViewers().isEmpty()) return;
    for (int i = 0; i < inventory.getSize(); i++) {
      if (inventory.getItem(i) == null) {
        setItem(i, background);
      }
    }
  }

  public void setItem(int slot, ItemStack item) {
    if (getInventory().getItem(slot) == null || !getInventory().getItem(slot).isSimilar(item)) {
      getInventory().setItem(slot, item);
    }
  }

  /**
   * @param event The event
   * @return For checking if {@link this#onInventoryClick(InventoryClickEvent)} happened within this
   *     GUI's inventory.
   */
  protected boolean wasClicked(InventoryClickEvent event) {
    if (!event.getInventory().equals(getInventory())) return false;

    final Inventory clickedInventory = event.getClickedInventory();
    if (clickedInventory == null || !clickedInventory.equals(getInventory())) return false;

    final HumanEntity humanEntity = event.getWhoClicked();
    return humanEntity instanceof Player;
  }

  public abstract void onInventoryClick(InventoryClickEvent event);
}
