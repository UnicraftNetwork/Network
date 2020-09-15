package cl.bgmp.lobbyx.util;

import org.bukkit.ChatColor;

public interface ChatUtil {

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
