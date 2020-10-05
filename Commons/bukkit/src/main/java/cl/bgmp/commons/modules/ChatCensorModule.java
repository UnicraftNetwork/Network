package cl.bgmp.commons.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatCensorModule extends Module {

  public ChatCensorModule() {
    super(ModuleId.CHAT_CENSOR);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    if (event.getPlayer().hasPermission(this.config.getChatCensorExemptPermission())) return;

    event.setCancelled(true);
  }

  @Override
  public boolean isEnabled() {
    return this.config.isChatCensored();
  }
}
