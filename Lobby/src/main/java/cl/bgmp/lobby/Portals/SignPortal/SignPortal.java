package cl.bgmp.lobby.Portals.SignPortal;

import cl.bgmp.lobby.Config;
import cl.bgmp.lobby.Lobby;
import cl.bgmp.lobby.Portals.BungeePortal;
import cl.bgmp.lobby.Portals.PortalType;
import cl.bgmp.utilsbukkit.Server;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignPortal extends BungeePortal implements Listener {
  private Sign sign;
  private SignRefreshTask signRefreshTask;

  public SignPortal(String id, Server server, Location location) {
    super(id, server, location, PortalType.SIGN);

    final BlockState blockState = Config.Spawn.getWorld().getBlockAt(this.getLocation()).getState();
    if (!(blockState instanceof Sign)) return;

    this.sign = (Sign) blockState;
    this.signRefreshTask = new SignRefreshTask(this);
    Lobby.get().registerEvents(this);
  }

  public SignRefreshTask getSignRefreshTask() {
    return signRefreshTask;
  }

  public void update(String line3) {
    final String line0 = ChatColor.GRAY + "Connect to:";
    final String line1 =
        ChatColor.WHITE + "[" + ChatColor.GOLD + server.getName() + ChatColor.WHITE + "]";

    sign.setLine(0, line0);
    sign.setLine(1, line1);
    sign.setLine(3, line3);
    sign.update(true);
  }

  @Override
  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {
    if (sign == null) return;

    final Player player = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    final BlockState blockState = event.getClickedBlock().getState();
    if (!(blockState instanceof org.bukkit.block.Sign)) return;
    if (blockState.equals(sign)) sendPlayerToServer(player);
  }

  @Override
  public void onPlayerInteractAtNPC(NPCInteractEvent event) {}
}
