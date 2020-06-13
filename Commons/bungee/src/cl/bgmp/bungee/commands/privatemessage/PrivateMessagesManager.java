package cl.bgmp.bungee.commands.privatemessage;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Util;
import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

// TODO: Sounds
public class PrivateMessagesManager implements Listener {
  public static HashMap<String, String> privateMessagesReplyRelations;

  public static HashMap<String, String> getReplyRelations() {
    return privateMessagesReplyRelations;
  }

  public static void sendMsg(
      @NotNull final ProxiedPlayer sender,
      @NotNull final ProxiedPlayer receiver,
      @NotNull String message) {

    getReplyRelations().put(sender.getName(), receiver.getName());
    getReplyRelations().put(receiver.getName(), sender.getName());

    final FlashComponent toMessage =
        new FlashComponent()
            .append(Util.resolveProxiedPlayerNick(receiver))
            .append(": ")
            .color(ChatColor.GRAY)
            .append(message)
            .color(ChatColor.WHITE);
    final FlashComponent fromMessage =
        new FlashComponent()
            .append(Util.resolveProxiedPlayerNick(sender))
            .append(": ")
            .color(ChatColor.GRAY)
            .append(message)
            .color(ChatColor.WHITE);

    sender.sendMessage(
        new FlashComponent(ChatConstant.MSG_PREFIX_TO.getAsString()).append(toMessage).build());
    sender.sendMessage(
        new FlashComponent(ChatConstant.MSG_PREFIX_FROM.getAsString()).append(fromMessage).build());
  }

  public static void sendReply(@NotNull final ProxiedPlayer sender, @NotNull String message) {
    final ProxiedPlayer receiver =
        CommonsBungee.get().getProxy().getPlayer(getReplyRelations().get(sender.getName()));

    if (receiver == null)
      sender.sendMessage(
          new FlashComponent(ChatConstant.NOTHING_TO_REPLY.getAsString())
              .color(ChatColor.RED)
              .build());
    else sendMsg(sender, receiver, message);
  }

  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    final ProxiedPlayer player = event.getPlayer();

    getReplyRelations().remove(player.getName());

    for (final String key : getReplyRelations().keySet()) {
      if (getReplyRelations().get(key).equals(player.getName())) {
        getReplyRelations().remove(key);
      }
    }
  }
}
