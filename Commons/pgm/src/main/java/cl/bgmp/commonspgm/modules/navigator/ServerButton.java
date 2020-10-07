package cl.bgmp.commonspgm.modules.navigator;

import cl.bgmp.butils.bungee.Bungee;
import cl.bgmp.butils.bungee.Server;
import cl.bgmp.butils.gui.GUIButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ServerButton extends GUIButton {
  private Bungee bungee;
  private Server server;

  public ServerButton(ItemStack itemStack, int slot, Bungee bungee, Server server) {
    super(itemStack, slot);
    this.bungee = bungee;
    this.server = server;
  }

  public Server getServer() {
    return server;
  }

  @Override
  public void clickBy(Player player) {
    this.bungee.sendPlayer(player, server.getName());
  }
}
