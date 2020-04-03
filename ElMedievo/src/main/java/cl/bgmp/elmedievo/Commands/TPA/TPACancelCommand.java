package cl.bgmp.elmedievo.Commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Teleport.TPA;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.elmedievo.Translations.Translator;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import java.util.List;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TPACancelCommand {

  // TODO: Allow players to cancel specific TPA requests
  @Command(
      aliases = {"tpcancel", "tpacancel"},
      desc = "Cancels one outgoing teleport request.",
      max = 0)
  public static void tpcancel(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    Player player = (Player) sender;
    Optional<TPA> tpa = ElMedievo.get().getTpaManager().getPlayerOutgoingTPA(player);

    if (tpa.isPresent()) {
      tpa.get().cancel();
      player.sendMessage(
          ChatColor.RED
              + ChatConstant.TPA_CANCEL
                  .getTranslatedTo(player.getLocale())
                  .replace("{0}", tpa.get().getPlayerTo().getDisplayName() + ChatColor.GREEN));
    } else player.sendMessage(ChatConstant.TPA_NO_PENDANT.getTranslatedTo(player.getLocale()));
  }

  @Command(
      aliases = {"tpcancelall", "tpacancelall"},
      desc = "Cancels all outgoing teleport request.",
      max = 0)
  public static void tpcancelall(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    Player player = (Player) sender;
    List<TPA> tpa = ElMedievo.get().getTpaManager().getAllPlayerOutgoingTPA(player);

    if (!tpa.isEmpty()) {
      tpa.forEach(TPA::cancel);
      player.sendMessage(
          ChatColor.GREEN
              + Translator.translate(player.getLocale(), ChatConstant.TPA_CANCELLED_ALL));
    } else
      player.sendMessage(
          ChatColor.RED + Translator.translate(player.getLocale(), ChatConstant.TPA_NO_PENDANT));
  }
}
