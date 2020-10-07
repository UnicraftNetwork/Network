package cl.bgmp.commonspgm.modules.navigator;

import cl.bgmp.butils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.meta.SkullMeta;

public class LegacyPlayerHeadBuilder extends ItemBuilder {

  public LegacyPlayerHeadBuilder(String owner) {
    super(Material.SKULL_ITEM);
    this.setDamage((short) SkullType.PLAYER.ordinal());

    SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
    skullMeta.setOwner(owner);
    this.itemStack.setItemMeta(skullMeta);
  }
}
