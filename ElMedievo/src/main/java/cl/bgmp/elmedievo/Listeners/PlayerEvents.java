package cl.bgmp.elmedievo.Listeners;

import cl.bgmp.utilsbukkit.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPermission("elmedievo.chat.color")) return;

    event.setMessage(Chat.colourify(event.getMessage()));
  }
}
