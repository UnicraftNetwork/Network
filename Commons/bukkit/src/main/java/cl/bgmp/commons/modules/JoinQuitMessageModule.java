package cl.bgmp.commons.modules;

import cl.bgmp.butils.chat.Chat;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageModule extends Module {

  public JoinQuitMessageModule() {
    super(ModuleId.JOINQUIT_MESSAGES);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (this.config.areJoinQuitMessagesSuppressed()) {
      event.setJoinMessage(null);
      return;
    }

    final Optional<Module> module = this.commons.getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module.isPresent()) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
      event.setJoinMessage(
          Chat.color(
                  this.config
                      .getJoinMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setJoinMessage(
          Chat.color(
                  this.config
                      .getJoinMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    if (this.config.areJoinQuitMessagesSuppressed()) {
      event.setQuitMessage(null);
      return;
    }

    final Optional<Module> module = this.commons.getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module.isPresent()) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
      event.setQuitMessage(
          Chat.color(
                  this.config
                      .getQuitMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setQuitMessage(
          Chat.color(
                  this.config
                      .getQuitMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @Override
  public boolean isEnabled() {
    return this.config.areJoinQuitMessagesEnabled();
  }
}
