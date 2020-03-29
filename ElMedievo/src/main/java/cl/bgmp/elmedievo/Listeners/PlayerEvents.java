package cl.bgmp.elmedievo.Listeners;

import cl.bgmp.utilsbukkit.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage(ChatColor.GREEN + "» " + player.getDisplayName());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    event.setQuitMessage(ChatColor.RED + "« " + event.getPlayer().getDisplayName());
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPermission("elmedievo.chat.color")) return;

    event.setMessage(Chat.colourify(event.getMessage()));
  }
}
