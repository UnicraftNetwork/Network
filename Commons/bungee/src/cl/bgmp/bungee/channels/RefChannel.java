package cl.bgmp.bungee.channels;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Permission;
import cl.bgmp.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RefChannel extends Channel {

  public RefChannel() {
    super(ChannelName.REF, Permission.REF_CHAT_SEE.getNode());
  }

  @Override
  public BaseComponent[] constructChannelMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
    final FlashComponent resolvedServerName = Util.resolveServerName(sender.getServer());
    final FlashComponent resolvedSenderNick = Util.resolveProxiedPlayerNick(sender);

    if (!sender.getServer().getInfo().getName().equals(receiver.getServer().getInfo().getName())) {
      return new FlashComponent(ChatConstant.REF_CHAT_PREFIX.getAsString())
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
    } else
      return new FlashComponent(ChatConstant.REF_CHAT_PREFIX.getAsString())
          .append(resolvedSenderNick)
          .append(": ")
          .color(ChatColor.WHITE)
          .append(message)
          .color(ChatColor.WHITE)
          .build();
  }
}
