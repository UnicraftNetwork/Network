package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class ForceGamemodeModule extends Module {

  public ForceGamemodeModule() {
    super(ModuleId.FORCE_GAMEMODE, Commons.get().getConfiguration().isForceGamemodeEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (event
        .getPlayer()
        .hasPermission(Commons.get().getConfiguration().getForceGamemodeExemptPerm())) return;
    event.getPlayer().setGameMode(Commons.get().getConfiguration().getForcedGamemode());
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {
    setEnabled(Commons.get().getConfiguration().isForceGamemodeEnabled());
    Commons.get().unregisterEvents(this);
  }
}
