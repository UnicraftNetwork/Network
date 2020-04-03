package cl.bgmp.elmedievo.Commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Teleport.TPA;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TPAcceptCommand {

  @Command(
      aliases = {"tpaccept", "tpaaccept"},
      desc = "Accepts one incoming teleport request.",
      usage = "<> | <player>",
      max = 1)
  public static void tpaccept(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    final Player player = (Player) sender;
    final Optional<TPA> tpa;

    if (args.argsLength() != 0) {
      final String targetName = args.getString(0);
      final Player target = Bukkit.getPlayer(targetName);
      if (target == null) {
        sender.sendMessage(
            ChatColor.RED + ChatConstant.INVALID_PLAYER.getTranslatedTo(player.getLocale()));
        return;
      }

      tpa = ElMedievo.get().getTpaManager().getPlayerSpecificIncomingTPA(player, target);
    } else tpa = ElMedievo.get().getTpaManager().getPlayerIncomingTPA(player);

    if (tpa.isPresent()) tpa.get().accept();
    else
      player.sendMessage(
          ChatColor.RED + ChatConstant.TPA_NO_PENDANT.getTranslatedTo(player.getLocale()));
  }
}
