package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

public class ChatFormatModule extends Module {
  // Placeholder constants

  private static final String NAME = "{name}";
  private static final String DISPLAY_NAME = "{displayname}";
  private static final String MESSAGE = "{message}";
  private static final String PREFIX = "{prefix}";
  private static final String SUFFIX = "{suffix}";
  public static final String DEFAULT_FORMAT = "<" + PREFIX + NAME + SUFFIX + "> " + MESSAGE;

  private String format;
  private Chat vaultChat = null;
  private Logger logger;

  public ChatFormatModule() {
    super(ModuleId.CHAT_FORMAT, Config.ChatFormat.isEnabled());
  }

  public void reloadConfigValues() {
    Config.reload();
    format =
        cl.bgmp.utilsbukkit.Chat.colourify(
            Config.ChatFormat.getFormat().replace(DISPLAY_NAME, "%1$s").replace(MESSAGE, "%2$s"));
  }

  public void refreshVault() {
    Chat vaultChat = Bukkit.getServer().getServicesManager().load(Chat.class);
    if (vaultChat != this.vaultChat)
      logger.info(
          "New Vault Chat implementation registered: "
              + (vaultChat == null ? "null" : vaultChat.getName()));
    this.vaultChat = vaultChat;
  }

  @EventHandler
  public void onServiceChange(ServiceRegisterEvent e) {
    if (e.getProvider().getService() == Chat.class) {
      refreshVault();
    }
  }

  @EventHandler
  public void onServiceChange(ServiceUnregisterEvent e) {
    if (e.getProvider().getService() == Chat.class) {
      refreshVault();
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChatLow(AsyncPlayerChatEvent e) {
    e.setFormat(format);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChatHigh(AsyncPlayerChatEvent e) {
    String format = e.getFormat();
    if (vaultChat != null && format.contains(PREFIX)) {
      format =
          format.replace(
              PREFIX, cl.bgmp.utilsbukkit.Chat.colourify(vaultChat.getPlayerPrefix(e.getPlayer())));
    }
    if (vaultChat != null && format.contains(SUFFIX)) {
      format =
          format.replace(
              SUFFIX, cl.bgmp.utilsbukkit.Chat.colourify(vaultChat.getPlayerSuffix(e.getPlayer())));
    }
    format = format.replace(NAME, e.getPlayer().getName());
    e.setFormat(format);
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {}
}
