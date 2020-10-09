package cl.bgmp.bungee.channels;

import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.MultiResolver;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Represents a chat channel */
public abstract class Channel {

  @Inject protected CommonsBungee commonsBungee;
  @Inject protected MultiResolver multiResolver;

  private Set<String> users = new HashSet<>();
  protected ChannelName name;
  protected String permission;

  public Channel(ChannelName name, String permission) {
    this.name = name;
    this.permission = permission;
  }

  public ChannelName getName() {
    return name;
  }

  public Set<String> getUsers() {
    return users;
  }

  public void enableFor(ProxiedPlayer player) {
    users.add(player.getName());
  }

  public void disableFor(ProxiedPlayer player) {
    users.remove(player.getName());
  }

  public boolean isEnabledFor(ProxiedPlayer player) {
    return users.contains(player.getName());
  }

  public void sendChannelMessage(ProxiedPlayer sender, String message) {
    for (ProxiedPlayer onlinePlayer : this.commonsBungee.getProxy().getPlayers()) {
      if (permission == null || !onlinePlayer.hasPermission(permission)) continue;
      onlinePlayer.sendMessage(constructChannelMessage(sender, onlinePlayer, message));
    }
  }

  /**
   * Wraps up the construction of a channel message
   *
   * @param sender Message sender
   * @param receiver Player who sees the message in chat
   * @param message The message sent
   * @return The constructed channel chat message: "[prefix] [Server] sender: message"
   */
  public abstract BaseComponent[] constructChannelMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message);
}
