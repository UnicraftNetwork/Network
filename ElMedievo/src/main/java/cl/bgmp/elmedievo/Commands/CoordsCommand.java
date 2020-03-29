package cl.bgmp.elmedievo.Commands;

import cl.bgmp.elmedievo.Sound.SoundManager;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.utilsbukkit.Chat;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CoordsCommand {

  @Command(
      aliases = {"coordinates", "coords"},
      desc = "Broadcasts the user's coordinates to the server chat.",
      max = 0)
  public static void coords(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    Player player = (Player) sender;
    Location origin = player.getLocation();

    Bukkit.broadcastMessage(
        (player.getDisplayName()
            + ChatColor.GRAY
            + ":"
            + Chat.NEW_LINE
            + ChatColor.YELLOW
            + "X: "
            + ChatColor.GRAY
            + origin.getBlockX()
            + Chat.NEW_LINE
            + ChatColor.YELLOW
            + "Y: "
            + ChatColor.GRAY
            + origin.getBlockY()
            + Chat.NEW_LINE
            + ChatColor.YELLOW
            + "Z: "
            + ChatColor.GRAY
            + origin.getBlockZ()));

    SoundManager.playSoundAtPlayer(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 3F);
  }
}
