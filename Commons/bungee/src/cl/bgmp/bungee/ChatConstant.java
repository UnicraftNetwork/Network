package cl.bgmp.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum ChatConstant {
  // Exceptions
  NO_CONSOLE(new TextComponent("You must be a player to execute this command.")),
  NO_PERMISSION(new TextComponent("You do not have permission.")),
  NUMBER_STRING_EXCEPTION(new TextComponent("Number expected, string received instead.")),
  ERROR(new TextComponent("An error has occurred. See console.")),
  SERVER_NOT_FOUND(new TextComponent("Could not find server ")),
  NOTHING_TO_REPLY(new TextComponent("Nothing to reply to.")),
  PLAYER_NOT_FOUND(new TextComponent("No player matched query.")),

  // Prefixes
  HELPOP_PREFIX(
      new TextComponent(ChatColor.WHITE + "[" + ChatColor.GOLD + "H" + ChatColor.WHITE + "] ")),
  MSG_PREFIX_FROM(
      new TextComponent(
          ChatColor.WHITE
              + "["
              + ChatColor.GOLD
              + "PM"
              + ChatColor.WHITE
              + "] "
              + ChatColor.GRAY
              + "From ")),
  MSG_PREFIX_TO(
      new TextComponent(
          ChatColor.WHITE
              + "["
              + ChatColor.GOLD
              + "PM"
              + ChatColor.WHITE
              + "] "
              + ChatColor.GRAY
              + "To ")),

  // ServerCommands strings
  LOBBY_TELEPORTING(new TextComponent("Teleporting you to the lobby...")),
  ADDED_SERVER(new TextComponent("Added Server ")),
  REMOVED_SERVER(new TextComponent("Removed Server ")),

  // Misc
  ARROW(new TextComponent("➔"));

  private TextComponent component;

  ChatConstant(final TextComponent component) {
    this.component = component;
  }

  public TextComponent getAsTextComponent() {
    return component;
  }

  public String getAsString() {
    return component.getText();
  }
}
