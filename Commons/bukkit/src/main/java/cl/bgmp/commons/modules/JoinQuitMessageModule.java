package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageModule extends Module {

  public JoinQuitMessageModule() {
    super(ModuleId.JOINQUIT_MESSAGES, Config.JoinQuitMessages.isEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    final Module module = Commons.get().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module != null) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module;
      event.setJoinMessage(
          Chat.colourify(
                  Config.JoinQuitMessages.getJoinMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setJoinMessage(
          Chat.colourify(
                  Config.JoinQuitMessages.getJoinMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    final Module module = Commons.get().getModule(ModuleId.CHAT_FORMAT);
    final Player player = event.getPlayer();

    if (module != null) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module;
      event.setQuitMessage(
          Chat.colourify(
                  Config.JoinQuitMessages.getQuitMessage()
                      .replaceAll("%prefix%", chatFormatModule.getPlayerPrefix(player))
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", chatFormatModule.getPlayerSuffix(player)));
    } else
      event.setQuitMessage(
          Chat.colourify(
                  Config.JoinQuitMessages.getQuitMessage()
                      .replaceAll("%prefix%", "")
                      .replaceAll("%player%", player.getName()))
              .replaceAll("%suffix%", ""));
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
    else Commons.get().registerEvents(new JoinQuitMessagesSuppressor());
  }

  @Override
  public void unload() {}

  // Suppresses join & quit messages. A quick workaround to allow Commons-Bungee to handle them
  // instead
  private class JoinQuitMessagesSuppressor implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
      event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
      event.setQuitMessage(null);
    }
  }
}
