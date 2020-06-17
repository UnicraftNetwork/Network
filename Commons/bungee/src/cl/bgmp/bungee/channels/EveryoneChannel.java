package cl.bgmp.bungee.channels;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class EveryoneChannel extends Channel {

  public EveryoneChannel() {
    super(ChannelName.EVERYONE, null);
  }

  @Override
  public BaseComponent[] constructChannelMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
    return new BaseComponent[0]; // ignored
  }
}
