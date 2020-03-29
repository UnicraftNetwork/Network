package cl.bgmp.utilsbukkit;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface Items {
  static ItemStack titledItemStack(Material material, String title) {
    final ItemStack itemStack = new ItemStack(material);
    final ItemMeta itemMeta = itemStack.getItemMeta();

    itemMeta.setDisplayName(title);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  static ItemStack titledItemStackWithLore(Material material, String title, String[] lore) {
    ItemStack itemStack = titledItemStack(material, title);
    ItemMeta itemMeta = itemStack.getItemMeta();

    int i = 0;
    for (String line : lore) {
      lore[i] = line;
      i++;
    }

    itemMeta.setLore(Arrays.asList(lore));
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  static ItemStack titledItemStackWithLore(Material material, String title, List<String> lore) {
    return titledItemStackWithLore(material, title, lore.toArray(new String[0]));
  }

  static void ensureItemObtainment(Player player, ItemStack itemStack) {
    if (player.getInventory().firstEmpty() == -1)
      player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    else player.getInventory().addItem(itemStack);
  }
}
