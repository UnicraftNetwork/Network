package cl.bgmp.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum ChatConstant {
  // Exceptions
  NO_CONSOLE("You must be a player to execute this command."),
  NO_PERMISSION("You do not have permission."),
  NUMBER_STRING_EXCEPTION("Number expected, string received instead."),
  ERROR("An error has occurred. See console."),
  SERVER_NOT_FOUND("Could not find server "),
  NOTHING_TO_REPLY("Nothing to reply to."),
  PLAYER_NOT_FOUND("No player matched query."),

  // Prefixes
  HELPOP_PREFIX(ChatColor.WHITE + "[" + ChatColor.GOLD + "H" + ChatColor.WHITE + "] "),
  MSG_PREFIX_FROM(
      ChatColor.WHITE
          + "["
          + ChatColor.GOLD
          + "PM"
          + ChatColor.WHITE
          + "] "
          + ChatColor.GRAY
          + "From "),
  MSG_PREFIX_TO(
      ChatColor.WHITE
          + "["
          + ChatColor.GOLD
          + "PM"
          + ChatColor.WHITE
          + "] "
          + ChatColor.GRAY
          + "To "
          + ChatColor.RESET),
  STAFF_CHAT_PREFIX(
      ChatColor.WHITE
          + "["
          + ChatColor.GOLD
          + "A"
          + ChatColor.WHITE
          + "] "
          + ChatColor.GRAY
          + "To "
          + ChatColor.RESET),

  // ServerCommands strings
  LOBBY_TELEPORTING("Teleporting you to the lobby..."),
  ADDED_SERVER("Added Server "),
  REMOVED_SERVER("Removed Server "),

  // Success messages
  HELPOP_SENT("Your message has been sent to the server staff."),

  // Misc
  ARROW("➔");

  private String text;

  ChatConstant(final String text) {
    this.text = text;
  }

  public TextComponent getAsTextComponent() {
    return new TextComponent(text);
  }

  public String getAsString() {
    return text;
  }
}
