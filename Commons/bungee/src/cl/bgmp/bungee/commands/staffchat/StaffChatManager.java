package cl.bgmp.bungee.commands.staffchat;

import cl.bgmp.bungee.BungeeMessages;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.ChatState;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.Permission;
import cl.bgmp.bungee.Util;
import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class StaffChatManager implements Listener {

  public static HashMap<String, ChatState> getPlayerChatStates() {
    return CommonsBungee.getPlayerChatStates();
  }

  public static void alternatePlayerChat(ProxiedPlayer player) {
    final ChatState alternation =
        getPlayerChatStates().get(player.getName()) == ChatState.GLOBAL
            ? ChatState.STAFF
            : ChatState.GLOBAL;
    final ChatConstant message =
        alternation == ChatState.GLOBAL
            ? ChatConstant.LOCKED_GLOBAL_CHAT
            : ChatConstant.LOCKED_STAFF_CHAT;

    getPlayerChatStates().put(player.getName(), alternation);
    player.sendMessage(
        BungeeMessages.append(
            ChatConstant.STAFF_CHAT_PREFIX.getAsTextComponent(), message.getAsString()));
  }

  public static void sendStaffChatMsg(@NotNull ProxiedPlayer sender, @NotNull String message) {
    for (ProxiedPlayer onlinePlayer : CommonsBungee.get().getProxy().getPlayers()) {
      if (onlinePlayer.hasPermission(Permission.STAFF_CHAT_SEE.getNode())) {
        onlinePlayer.sendMessage(constructStaffChatMessage(sender, onlinePlayer, message));
      }
    }
  }

  @EventHandler
  public void onPlayerChat(ChatEvent event) {
    if (event.isCommand() || !(event.getSender() instanceof ProxiedPlayer)) return;

    final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
    if (getPlayerChatStates().get(player.getName()) == ChatState.GLOBAL) return;

    event.setCancelled(true);

    for (ProxiedPlayer onlinePlayer : CommonsBungee.get().getProxy().getPlayers()) {
      if (onlinePlayer.hasPermission(Permission.STAFF_CHAT_SEE.getNode())) {
        onlinePlayer.sendMessage(
            constructStaffChatMessage(player, onlinePlayer, event.getMessage()));
      }
    }
  }

  @EventHandler
  public void onPlayerJoin(ServerConnectEvent event) {
    getPlayerChatStates().putIfAbsent(event.getPlayer().getName(), ChatState.GLOBAL);
  }

  /**
   * Wraps up the construction of a staff chat message
   *
   * @param sender Message sender
   * @param receiver Player who sees the message in chat
   * @param message The message sent
   * @return The constructed staff chat message: [A] [Server] sender: message
   */
  private static BaseComponent[] constructStaffChatMessage(
      ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
    TextComponent prefix =
        BungeeMessages.append(
            ChatConstant.STAFF_CHAT_PREFIX.getAsTextComponent(),
            Util.resolveServerName(sender.getServer()).getText());
    ComponentBuilder formattedStaffChatMessage = new ComponentBuilder(prefix);
    formattedStaffChatMessage.append(Util.resolveProxiedPlayerNick(sender, receiver));
    formattedStaffChatMessage.append(ChatColor.WHITE + ": " + message);
    return formattedStaffChatMessage.create();
  }
}
