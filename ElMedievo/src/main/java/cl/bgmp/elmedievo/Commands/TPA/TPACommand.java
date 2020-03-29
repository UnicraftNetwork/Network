package cl.bgmp.elmedievo.Commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Teleport.TPA;
import cl.bgmp.elmedievo.Teleport.TPAManager;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.elmedievo.Translations.Translator;
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
      min = 1,
      max = 1)
  public static void tpa(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    String playerToName = args.getString(0);
    Player playerTo = Bukkit.getPlayer(playerToName);

    if (playerTo == null) {
      sender.sendMessage(ChatColor.RED + Translator.translate(sender, ChatConstant.INVALID_PLAYER));
      return;
    }

    Player player = (Player) sender;
    TPAManager tpaManager = ElMedievo.get().getTpaManager();
    TPA tpa = new TPA(player, playerTo);

    if (tpaManager.getMatchingTPA(tpa).isPresent())
      sender.sendMessage(
          ChatColor.RED + Translator.translate(sender, ChatConstant.ALREADY_REQUESTING_TPA));
    else {
      tpa.send();
      player.sendMessage(
          ChatColor.GREEN
              + ChatConstant.TPA_SENT
                  .getTranslatedTo(player.getLocale())
                  .replace("{0}", tpa.getPlayerTo().getDisplayName() + ChatColor.GREEN));
    }
  }
}
