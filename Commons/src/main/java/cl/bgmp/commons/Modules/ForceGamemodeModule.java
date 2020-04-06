package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class ForceGamemodeModule extends Module {

  public ForceGamemodeModule() {
    super(ModuleId.FORCE_GAMEMODE, Config.ForceGamemode.isEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (event.getPlayer().hasPermission(Config.ForceGamemode.getGamemodeForceExemptPermission()))
      return;
    event.getPlayer().setGameMode(Config.ForceGamemode.getGamemode());
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {}
}
