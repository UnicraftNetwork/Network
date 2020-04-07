package cl.bgmp.elmedievo.Commands;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BackCommand {

  @Command(
      aliases = {"back"},
      desc = "Takes you back to the location of your last death.",
      max = 0)
  public static void back(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
    } else ElMedievo.get().getBackQueueManager().teleportPlayer((Player) sender);
  }
}
