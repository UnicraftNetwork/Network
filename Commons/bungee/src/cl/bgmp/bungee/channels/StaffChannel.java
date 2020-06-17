package cl.bgmp.bungee.channels;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Permission;
import cl.bgmp.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class StaffChannel extends Channel {

  public StaffChannel() {
    super(ChannelName.STAFF, Permission.STAFF_CHAT_SEE.getNode());
  }

  @Override
  public BaseComponent[] constructChannelMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
    final FlashComponent resolvedServerName = Util.resolveServerName(sender.getServer());
    final FlashComponent resolvedSenderNick = Util.resolveProxiedPlayerNick(sender);

    return new FlashComponent(ChatConstant.STAFF_CHAT_PREFIX.getAsString())
        .append("[")
        .color(ChatColor.WHITE)
        .append(resolvedServerName)
        .append("] ")
        .color(ChatColor.WHITE)
        .append(resolvedSenderNick)
        .append(": ")
        .color(ChatColor.WHITE)
        .append(message)
        .color(ChatColor.WHITE)
        .build();
  }
}
