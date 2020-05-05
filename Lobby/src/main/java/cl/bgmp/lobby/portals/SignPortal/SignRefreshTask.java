package cl.bgmp.lobby.portals.SignPortal;

import cl.bgmp.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class SignRefreshTask extends BukkitRunnable {
  private SignPortal signPortal;

  public SignRefreshTask(SignPortal signPortal) {
    this.signPortal = signPortal;
    this.runTaskTimer(Lobby.get(), 0L, 100L);
  }

  @Override
  public void run() {
    // TODO: This shouldn't be hardcoded
    signPortal.update(ChatColor.AQUA + "Towny");
  }
}
