package cl.bgmp.lobbyx.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public interface ChatUtil {

  static void log(CommandSender player, String msg) {
    player.sendMessage(
        ChatColor.WHITE + "[" + ChatColor.AQUA + "LobbyX" + ChatColor.WHITE + "] " + msg);
  }

  static String lobbyXHeader() {
    return ChatColor.BLUE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "-----------"
        + ChatColor.AQUA
        + " LobbyX "
        + ChatColor.BLUE
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "-----------";
  }

  static String lobbyXFooter() {
    return ChatColor.BLUE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "----------------------------";
  }
}
