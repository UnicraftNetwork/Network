package cl.bgmp.bungee.privatemessages;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PrivateMessagesManager implements Listener {
  private final CommonsBungee commonsBungee;
  private final MultiResolver multiResolver;
  private final HashMap<String, String> replyRelations = new HashMap<>();

  public PrivateMessagesManager(CommonsBungee commonsBungee, MultiResolver multiResolver) {
    this.commonsBungee = commonsBungee;
    this.multiResolver = multiResolver;

    this.commonsBungee.registerEvent(this);
  }

  public void sendMsg(final ProxiedPlayer sender, final ProxiedPlayer receiver, String message) {

    this.replyRelations.put(sender.getName(), receiver.getName());
    this.replyRelations.put(receiver.getName(), sender.getName());

    final ComponentWrapper toMessage =
        new ComponentWrapper()
            .append(this.multiResolver.resolveProxiedPlayerNick(receiver))
            .append(": ")
            .color(ChatColor.GRAY)
            .append(message)
            .color(ChatColor.WHITE);
    final ComponentWrapper fromMessage =
        new ComponentWrapper()
            .append(this.multiResolver.resolveProxiedPlayerNick(sender))
            .append(": ")
            .color(ChatColor.GRAY)
            .append(message)
            .color(ChatColor.WHITE);

    sender.sendMessage(
        new ComponentWrapper(ChatConstant.MSG_PREFIX_TO.getAsString()).append(toMessage).build());
    receiver.sendMessage(
        new ComponentWrapper(ChatConstant.MSG_PREFIX_FROM.getAsString())
            .append(fromMessage)
            .build());
  }

  public void sendReply(final ProxiedPlayer sender, String message) {
    final ProxiedPlayer receiver =
        this.commonsBungee.getProxy().getPlayer(this.replyRelations.get(sender.getName()));

    if (receiver == null)
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.NOTHING_TO_REPLY.getAsString())
              .color(ChatColor.RED)
              .build());
    else this.sendMsg(sender, receiver, message);
  }

  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    final ProxiedPlayer player = event.getPlayer();

    this.replyRelations.remove(player.getName());

    for (final String key : this.replyRelations.keySet()) {
      if (this.replyRelations.get(key).equals(player.getName())) {
        this.replyRelations.remove(key);
        break;
      }
    }
  }
}
