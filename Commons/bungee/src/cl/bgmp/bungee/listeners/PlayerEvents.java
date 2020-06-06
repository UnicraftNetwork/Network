package cl.bgmp.bungee.listeners;

import cl.bgmp.bungee.BungeeMessages;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerSwitchServer(ServerSwitchEvent event) {
    final ServerInfo from = event.getFrom();
    final ServerInfo to = event.getPlayer().getServer().getInfo();
    final ProxiedPlayer player = event.getPlayer();

    final String serverSwitchString =
        Util.resolveServerName(from).getText()
            + ChatColor.YELLOW
            + ChatConstant.DOUBLE_ARROWS.getAsString()
            + " "
            + Util.resolveServerName(to).getText();
    player.sendMessage(
        BungeeMessages.append(
            new TextComponent(serverSwitchString),
            Util.resolveProxiedPlayerNick(player, player).getText()));
  }
}
