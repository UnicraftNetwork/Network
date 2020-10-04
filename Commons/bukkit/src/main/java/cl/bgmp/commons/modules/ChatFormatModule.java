package cl.bgmp.commons.modules;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServiceRegisterEvent;

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

  public ChatFormatModule() {
    super(ModuleId.CHAT_FORMAT);
  }

  public String getPlayerPrefix(final Player player) {
    return vaultChat.getPlayerPrefix(player);
  }

  public String getPlayerSuffix(final Player player) {
    return vaultChat.getPlayerSuffix(player);
  }

  public void reloadConfigValues() {
    this.commons.reloadConfig();
    format =
        cl.bgmp.butils.chat.Chat.color(
            this.config.getVaultFormat().replace(DISPLAY_NAME, "%1$s").replace(MESSAGE, "%2$s"));
  }

  public void refreshVault() {
    Chat vaultChat = Bukkit.getServer().getServicesManager().load(Chat.class);
    if (vaultChat != this.vaultChat)
      this.commons
          .getLogger()
          .info(
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
              PREFIX, cl.bgmp.butils.chat.Chat.color(vaultChat.getPlayerPrefix(e.getPlayer())));
    }
    if (vaultChat != null && format.contains(SUFFIX)) {
      format =
          format.replace(
              SUFFIX, cl.bgmp.butils.chat.Chat.color(vaultChat.getPlayerSuffix(e.getPlayer())));
    }
    format = format.replace(NAME, e.getPlayer().getName());
    e.setFormat(format);
  }

  @Override
  public void configure() {
    this.format = this.config.getVaultFormat();
  }

  @Override
  public boolean isEnabled() {
    return this.config.isVaultFormattingEnabled();
  }

  @Override
  public void load() {
    if (this.isEnabled()) {
      refreshVault();
      reloadConfigValues();
      this.commons.registerEvent(this);
    }
  }

  @Override
  public void unload() {
    this.commons.unregisterEvent(this);
  }
}
