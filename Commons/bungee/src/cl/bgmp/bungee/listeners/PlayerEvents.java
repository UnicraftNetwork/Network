package cl.bgmp.bungee.listeners;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import com.google.inject.Inject;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerEvents implements Listener {

  private final CommonsBungee commonsBungee;
  private final MultiResolver multiResolver;

  @Inject
  public PlayerEvents(CommonsBungee commonsBungee, MultiResolver multiResolver) {
    this.multiResolver = multiResolver;
    this.commonsBungee = commonsBungee;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerSwitchServer(ServerSwitchEvent event) {
    final ServerInfo from = event.getFrom();
    final ServerInfo to = event.getPlayer().getServer().getInfo();
    final ProxiedPlayer player = event.getPlayer();

    final BaseComponent[] prefix;
    final String action;

    if (from == null) {
      prefix =
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(this.multiResolver.resolveServerName(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .build();
      action = ChatConstant.JOINED_THE_GAME.getAsString();
    } else {
      prefix =
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(this.multiResolver.resolveServerName(from))
              .append(" ")
              .append(ChatConstant.DOUBLE_ARROWS.getAsString())
              .append(" ")
              .append(this.multiResolver.resolveServerName(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .build();
      action = ChatConstant.CHANGED_SERVERS.getAsString();
    }

    player.sendMessage(
        new ComponentWrapper()
            .append(prefix)
            .append(player.getDisplayName())
            .color(ChatColor.DARK_AQUA)
            .append(" " + action)
            .color(ChatColor.YELLOW)
            .build());
  }

  // Just a reminder of how it should look/work like. As the user disconnects, they won't be able to
  // see the message, but in the future, this will be sent to other players as well, players who
  // will be able to see it
  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();
    player.sendMessage(
        new ComponentWrapper("[")
            .color(ChatColor.WHITE)
            .append(
                this.multiResolver
                    .resolveServerName(player.getServer())
                    .append(player.getName())
                    .color(ChatColor.DARK_AQUA)
                    .append(" " + ChatConstant.LEFT_THE_GAME.getAsString())
                    .color(ChatColor.YELLOW))
            .build());
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

    final ServerInfo to = this.multiResolver.resolveSuitableLobby();
    if (from.equals(to)) return;

    event.setCancelled(true);
    event.setCancelServer(to);

    player.sendMessage(
        new ComponentWrapper(ChatConstant.SERVER_KICK.getAsString()).color(ChatColor.RED).build());
  }
}
