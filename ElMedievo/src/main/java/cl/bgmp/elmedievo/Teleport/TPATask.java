package cl.bgmp.elmedievo.Teleport;

import cl.bgmp.elmedievo.ElMedievo;
import org.bukkit.scheduler.BukkitRunnable;

public class TPATask extends BukkitRunnable {
  private TPA tpa;

  public TPATask(TPA tpa) {
    this.tpa = tpa;
    this.runTaskTimerAsynchronously(
        ElMedievo.get(), tpa.getExpiry(), 0L); // TPA::expire() in 2 minutes
  }

  @Override
  public void run() {
    tpa.expire();
  }
}
