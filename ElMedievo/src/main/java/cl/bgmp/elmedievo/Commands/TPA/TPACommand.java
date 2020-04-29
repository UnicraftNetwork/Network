package cl.bgmp.elmedievo.Commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Teleport.TPA;
import cl.bgmp.elmedievo.Teleport.TPAManager;
import cl.bgmp.utilsbukkit.Translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TPACommand {

  @Command(
      aliases = {"tpa"},
      desc = "Sends a teleport request to a player.",
      usage = "<player>",
      min = 1,
      max = 1)
  public static void tpa(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + Translations.get("commands.no.console", sender));
      return;
    }

    String playerToName = args.getString(0);
    Player playerTo = Bukkit.getPlayer(playerToName);

    if (playerTo == null) {
      sender.sendMessage(ChatColor.RED + Translations.get("misc.invalid.player", sender));
      return;
    }

    TPAManager tpaManager = ElMedievo.get().getTpaManager();
    Player player = (Player) sender;
    TPA tpa = new TPA(player, playerTo);

    if (tpaManager.getMatchingTPA(tpa).isPresent())
      sender.sendMessage(ChatColor.RED + Translations.get("tpa.already.requesting", player));
    else {
      tpa.send();
      player.sendMessage(
          ChatColor.GREEN
              + Translations.get(
                  "tpa.sent", player, tpa.getPlayerTo().getDisplayName() + ChatColor.GREEN));
    }
  }
}
