package cl.bgmp.commons.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class ForceGamemodeModule extends Module {

  public ForceGamemodeModule() {
    super(ModuleId.FORCE_GAMEMODE);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (event.getPlayer().hasPermission(this.config.getForceGamemodeExemptPerm())) return;
    event.getPlayer().setGameMode(this.config.getForcedGamemode());
  }

  @Override
  public boolean isEnabled() {
    return this.config.isForceGamemodeEnabled();
  }
}
