package cl.bgmp.utilsbukkit;

import org.bukkit.ChatColor;

public interface Chat {
  String NEW_LINE = "\n";

  static String colourify(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  static String buildHeader(String title) {
    return ChatColor.BLUE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "-----------------"
        + ChatColor.WHITE
        + " "
        + title
        + " "
        + ChatColor.BLUE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "-----------------";
  }

  static String getStringAsException(String string) {
    return ChatColor.YELLOW + "⚠ " + ChatColor.RED + string;
  }
}
