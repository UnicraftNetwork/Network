package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabModule extends Module {

  public TabModule() {
    super(ModuleId.TAB, Config.Tab.isEnabled());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final Module module = Commons.get().getModule(ModuleId.CHAT_FORMAT);

    if (module != null) {
      final ChatFormatModule chatFormatModule = (ChatFormatModule) module;
      final Chat vaultChat = chatFormatModule.getVaultChat();

      if (vaultChat != null) {
        player.setPlayerListName(
            cl.bgmp.utilsbukkit.Chat.colourify(
                vaultChat.getPlayerPrefix(player)
                    + Config.Tab.getNicksColourCode()
                    + player.getName()));
        return;
      }
    }

    player.setPlayerListName(
        cl.bgmp.utilsbukkit.Chat.colourify(Config.Tab.getNicksColourCode() + player.getName()));
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {}
}
