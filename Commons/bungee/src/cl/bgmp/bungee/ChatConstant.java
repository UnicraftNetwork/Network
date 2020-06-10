package cl.bgmp.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum ChatConstant {
  // Exceptions
  NO_CONSOLE("You must be a player to execute this command."),
  NO_PERMISSION("You do not have permission."),
  NUMBER_STRING_EXCEPTION("Number expected, string received instead."),
  ERROR("An error has occurred. See console."),
  NOTHING_TO_REPLY("Nothing to reply to."),
  PLAYER_NOT_FOUND("No players matched query."),
  NO_LOBBIES_AVAILABLE("No lobbies are available at this moment."),
  SERVER_KICK(
      "The server you were previously on went offline. You have been connected to a fallback server."),

  // Prefixes
  HELPOP_PREFIX(
      ChatColor.WHITE + "[" + ChatColor.GOLD + "H" + ChatColor.WHITE + "] " + ChatColor.RESET),
  MSG_PREFIX_FROM(
      ChatColor.WHITE
          + "["
          + ChatColor.GOLD
          + "PM"
          + ChatColor.WHITE
          + "] "
          + ChatColor.GRAY
          + "From "
          + ChatColor.RESET),
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
      ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + ChatColor.RESET),

  // ServerCommands strings
  TELEPORTING("Teleporting..."),

  // Success messages
  HELPOP_SENT("Your message has been sent to the server staff."),
  CHANGED_SERVERS("changed servers"),
  JOINED_THE_GAME("joined the game"),
  LEFT_THE_GAME("left the game"),
  LOCKED_STAFF_CHAT("Now locked into staff chat mode"),
  LOCKED_GLOBAL_CHAT("Now locked into global chat mode"),

  // Misc
  ARROW("➔"),
  DOUBLE_ARROWS("»");

  private final String text;

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
