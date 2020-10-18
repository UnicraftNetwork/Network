package cl.bgmp.bungee.listeners;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import com.google.inject.Inject;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class PlayerEvents implements Listener {

  private final CommonsBungee commonsBungee;
  private final MultiResolver multiResolver;

  @Inject
  public PlayerEvents(CommonsBungee commonsBungee, MultiResolver multiResolver) {
    this.multiResolver = multiResolver;
    this.commonsBungee = commonsBungee;
  }

  @EventHandler
  public void onPlayerKickedFromServer(ServerKickEvent event) {
    final ProxiedPlayer player = event.getPlayer();

    ServerInfo from;
    if (event.getPlayer().getServer() != null) {
      from = player.getServer().getInfo();
    } else if (this.commonsBungee.getProxy().getReconnectHandler() == null) {

      from = this.commonsBungee.getProxy().getReconnectHandler().getServer(player);
    } else {
      from = AbstractReconnectHandler.getForcedHost(player.getPendingConnection());
      if (from == null)
        from =
            ProxyServer.getInstance()
                .getServerInfo(player.getPendingConnection().getListener().getDefaultServer());
    }

    if (from == null) return;

    final Optional<ServerInfo> to = this.multiResolver.resolveSuitableLobby();
    if (!to.isPresent() || from.equals(to.get())) return;

    event.setCancelled(true);
    event.setCancelServer(to.get());

    player.sendMessage(
        new ComponentWrapper(ChatConstant.SERVER_KICK.getAsString()).color(ChatColor.RED).build());
  }
}
