package cl.bgmp.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Only minor tweaks for organising {@link BungeeMessages} have been made. Credit goes to the
 * original resource listed beneath
 *
 * @author https://github.com/applenick/BungeeUtils
 */
public class BungeeMessages {

  public static TextComponent colourify(ChatColor colour, TextComponent textComponent) {
    textComponent.setColor(colour);
    return textComponent;
  }

  public static TextComponent append(TextComponent original, String to) {
    original.addExtra(to);
    return original;
  }

  /**
   * sk89q's Command Framework messages
   *
   * @see CommonsBungee
   */
  static void commandPermissionsException(CommandSender sender) {
    TextComponent permissionException = ChatConstant.NO_PERMISSION.getAsTextComponent();
    permissionException.setColor(ChatColor.RED);

    sender.sendMessage(permissionException);
  }

  static void nestedCommandUsageException(CommandSender sender, String usage) {
    TextComponent nestedException = new TextComponent(usage);
    nestedException.setColor(ChatColor.RED);

    sender.sendMessage(nestedException);
  }

  static void commandUsageException(CommandSender sender, String message, String usage) {
    TextComponent usageMessage = new TextComponent(message);
    TextComponent usageException = new TextComponent(usage);

    usageMessage.setColor(ChatColor.RED);
    usageException.setColor(ChatColor.RED);

    sender.sendMessage(usageMessage);
    sender.sendMessage(usageException);
  }

  static void numberFormatException(CommandSender sender) {
    TextComponent numberFormat = ChatConstant.NUMBER_STRING_EXCEPTION.getAsTextComponent();
    numberFormat.setColor(ChatColor.RED);

    sender.sendMessage(numberFormat);
  }

  static void consoleError(CommandSender sender) {
    TextComponent consoleError = ChatConstant.ERROR.getAsTextComponent();
    consoleError.setColor(ChatColor.RED);

    sender.sendMessage(consoleError);
  }

  static void commandException(CommandSender sender, String exception) {
    TextComponent consoleError = new TextComponent(exception);
    consoleError.setColor(ChatColor.RED);

    sender.sendMessage(consoleError);
  }
}
