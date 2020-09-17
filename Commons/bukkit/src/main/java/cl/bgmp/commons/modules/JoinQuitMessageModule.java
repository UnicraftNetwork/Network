package cl.bgmp.commons.modules;

import cl.bgmp.butils.chat.Chat;
import cl.bgmp.commons.Commons;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageModule extends Module {

  public JoinQuitMessageModule() {
    super(
        ModuleId.JOINQUIT_MESSAGES, Commons.get().getConfiguration().areJoinQuitMessagesEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (Commons.get().getConfiguration().areJoinQuitMessagesSuppressed()) {
      event.setJoinMessage(null);
      return;
    }

    final Optional<Module> module =
        Commons.get().getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module.isPresent()) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
      event.setJoinMessage(
          Chat.color(
                  Commons.get()
                      .getConfiguration()
                      .getJoinMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setJoinMessage(
          Chat.color(
                  Commons.get()
                      .getConfiguration()
                      .getJoinMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    if (Commons.get().getConfiguration().areJoinQuitMessagesSuppressed()) {
      event.setQuitMessage(null);
      return;
    }

    final Optional<Module> module =
        Commons.get().getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module.isPresent()) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
      event.setQuitMessage(
          Chat.color(
                  Commons.get()
                      .getConfiguration()
                      .getQuitMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setQuitMessage(
          Chat.color(
                  Commons.get()
                      .getConfiguration()
                      .getQuitMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {
    setEnabled(Commons.get().getConfiguration().areJoinQuitMessagesEnabled());
    Commons.get().unregisterEvents(this);
  }
}
