package cl.bgmp.utilsbukkit.Items;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public interface PlayerHeads {
  @SuppressWarnings("deprecation")
  static ItemStack playerHead(String owner) {
    final ItemStack skullItem =
        new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
    final SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

    skullMeta.setOwner(owner);
    skullItem.setItemMeta(skullMeta);

    return skullItem;
  }

  static ItemStack titledPlayerHead(String owner, String title) {
    final ItemStack skullItem = playerHead(owner);
    final SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

    skullMeta.setDisplayName(title);
    skullItem.setItemMeta(skullMeta);

    return skullItem;
  }

  static ItemStack titledPlayerHeadWithLore(String owner, String title, String[] lore) {
    final ItemStack skullItem = titledPlayerHead(owner, title);
    final SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

    skullMeta.setLore(Arrays.asList(lore));
    skullItem.setItemMeta(skullMeta);

    return skullItem;
  }
}
