package cl.bgmp.commons.navigator;

import cl.bgmp.butils.bungee.Server;
import cl.bgmp.butils.gui.GUIButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ServerButton extends GUIButton {
  private Server server;

  public ServerButton(ItemStack itemStack, int slot, Server server) {
    super(itemStack, slot);
    this.server = server;
  }

  public Server getServer() {
    return server;
  }

  @Override
  public abstract void clickBy(Player player);
}
