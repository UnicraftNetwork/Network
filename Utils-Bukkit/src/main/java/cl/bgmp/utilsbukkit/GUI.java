package cl.bgmp.utilsbukkit;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class GUI {
  private Inventory inventory;
  private String title;
  private ItemStack background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

  public GUI(Plugin plugin, String title, int size) {
    this.title = title;
    this.inventory = Bukkit.createInventory(null, size, title);

    Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::addContent, 0L, 5L);
  }

  public GUI(Plugin plugin, String title, int size, ItemStack background) {
    this.title = title;
    this.inventory = Bukkit.createInventory(null, size, title);
    this.background = background;

    Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::addContent, 0L, 5L);
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
      if (inventory.getItem(i) == null
          || Objects.requireNonNull(inventory.getItem(i)).getType().equals(Material.AIR)) {
        setItem(i, background);
      }
    }
  }

  public void setItem(int slot, ItemStack item) {
    if (getInventory().getItem(slot) == null
        || !Objects.requireNonNull(getInventory().getItem(slot)).isSimilar(item)) {
      getInventory().setItem(slot, item);
    }
  }

  public abstract void onInventoryClick(InventoryClickEvent event);
}
