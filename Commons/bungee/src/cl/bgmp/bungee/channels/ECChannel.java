package cl.bgmp.bungee.channels;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.Permission;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ECChannel extends Channel {

  public ECChannel() {
    super(ChannelName.EC, Permission.EC_CHAT_SEE.getNode());
  }

  @Override
  public BaseComponent[] constructChannelMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
    final ComponentWrapper resolvedServerName =
        this.multiResolver.getClickableNameOf(sender.getServer());
    final ComponentWrapper resolvedSenderNick = this.multiResolver.resolveProxiedPlayerNick(sender);

    if (!sender.getServer().getInfo().getName().equals(receiver.getServer().getInfo().getName())) {
      return new ComponentWrapper(ChatConstant.EC_CHAT_PREFIX.getAsString())
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
      return new ComponentWrapper(ChatConstant.EC_CHAT_PREFIX.getAsString())
          .append(resolvedSenderNick)
          .append(": ")
          .color(ChatColor.WHITE)
          .append(message)
          .color(ChatColor.WHITE)
          .build();
  }
}
