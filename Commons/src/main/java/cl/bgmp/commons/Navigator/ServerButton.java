package cl.bgmp.commons.Navigator;

import cl.bgmp.utilsbukkit.Server;
import org.bukkit.inventory.ItemStack;

public class ServerButton {
  private Server server;
  private ItemStack item;
  private int slot;

  public ServerButton(Server server, ItemStack item, int slot) {
    this.server = server;
    this.item = item;
    this.slot = slot;
  }

  public Server getServer() {
    return server;
  }

  public ItemStack getItem() {
    return item;
  }

  public int getSlot() {
    return slot;
  }
}
