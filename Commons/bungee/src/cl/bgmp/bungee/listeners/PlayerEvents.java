package cl.bgmp.bungee.listeners;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Util;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerSwitchServer(ServerSwitchEvent event) {
    final ServerInfo from = event.getFrom();
    final ServerInfo to = event.getPlayer().getServer().getInfo();
    final ProxiedPlayer player = event.getPlayer();

    if (from == null) {
      player.sendMessage(
          new FlashComponent("[")
              .color(ChatColor.WHITE)
              .append(Util.resolveServerName(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .append(player.getDisplayName())
              .append(" " + ChatConstant.JOINED_THE_GAME.getAsString())
              .color(ChatColor.YELLOW)
              .build());
    } else {
      player.sendMessage(
          new FlashComponent("[")
              .color(ChatColor.WHITE)
              .append(Util.resolveServerName(from))
              .append(" ")
              .append(ChatConstant.DOUBLE_ARROWS.getAsString())
              .append(" ")
              .append(Util.resolveServerName(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .append(player.getDisplayName())
              .append(" " + ChatConstant.CHANGED_SERVERS.getAsString())
              .color(ChatColor.YELLOW)
              .build());
    }
  }

  // Just a reminder of how it should look/work like. As the user disconnects, they won't be able to
  // see the message, but in the future, this will be sent to other players as well, players who
  // will be able to see it
  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();
    player.sendMessage(
        new FlashComponent("[")
            .color(ChatColor.WHITE)
            .append(
                Util.resolveServerName(player.getServer())
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
    } else if (CommonsBungee.get().getProxy().getReconnectHandler() == null) {
      from = CommonsBungee.get().getProxy().getReconnectHandler().getServer(player);
    } else {
      from = AbstractReconnectHandler.getForcedHost(player.getPendingConnection());
      if (from == null)
        from =
            ProxyServer.getInstance()
                .getServerInfo(player.getPendingConnection().getListener().getDefaultServer());
    }

    final ServerInfo to = Util.resolveSuitableLobby();
    if (from != null && from.equals(to)) return;

    event.setCancelled(true);
    event.setCancelServer(to);

    player.sendMessage(
        new FlashComponent(ChatConstant.SERVER_KICK.getAsString()).color(ChatColor.RED).build());
  }
}
