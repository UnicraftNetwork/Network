package cl.bgmp.commons.commands;

import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.annotations.Command;
import com.sk89q.minecraft.util.commands.annotations.CommandPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// FIXME: Abstract hard-coded stuffs beneath
public class PurchaseNotificationCommand {
  @Command(
      aliases = {"notifypurchase"},
      desc = "Utility command for sending notifications upon shop purchases.",
      min = 2,
      max = 2)
  @CommandPermissions("commons.notifypurchase")
  public static void notifypurchase(final CommandContext args, final CommandSender sender) {
    if (sender instanceof Player) {
      sender.sendMessage(ChatColor.RED + Translations.get("commands.internal", sender));
      return;
    }

    String rankName = args.getString(0);
    String playerName = args.getString(1);

    switch (rankName) {
      case "perritaoveja":
        rankName = "&a&lPERRITAOVEJA&r";
        break;
      case "purokes":
        rankName = "&2&lPUROKES&r";
        break;
      case "medievo":
        rankName = "&6&lMEDIEVO&r";
        break;
      case "exclusive":
        rankName = "&d\u2764&e"; // UTF-8 heart icon <3
        break;
    }

    Bukkit.broadcastMessage(
        Chat.colourify(
            "&b&lSHOP &a» &7El usuario&e "
                + playerName
                + "&7 ha comprado el rango "
                + rankName
                + "&7 en nuestra tienda online!"));
  }
}
