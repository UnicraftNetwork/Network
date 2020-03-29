package cl.bgmp.lobby;

import cl.bgmp.lobby.Listeners.BlockEvents;
import cl.bgmp.lobby.Listeners.PlayerEvents;
import cl.bgmp.lobby.Portals.PortalFactory;
import cl.bgmp.utilsbukkit.Channels;
import net.jitse.npclib.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends JavaPlugin {
  private static Lobby lobby;
  private PortalFactory portalFactory;
  private NPCLib npcLib;

  public static Lobby get() {
    return lobby;
  }

  public NPCLib getNPCLib() {
    return npcLib;
  }

  @Override
  public void onEnable() {
    lobby = this;
    loadConfiguration();

    npcLib = new NPCLib(this);
    portalFactory = new PortalFactory();
    portalFactory.loadPortals();

    Channels.registerBungeeToPlugin(this);
    registerEvents(new BlockEvents(), new PlayerEvents());
  }

  @Override
  public void onDisable() {}

  public void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  public void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
