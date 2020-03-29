package cl.bgmp.elmedievo.Commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Teleport.TPA;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.utilsbukkit.Chat;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TPAList {
  @Command(
      aliases = {"tplist", "tpalist"},
      desc = "Lists all incoming and outgoing teleport requests.",
      max = 0)
  public static void tpalist(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getString());
      return;
    }

    Player player = (Player) sender;
    List<TPA> incomingTPA = ElMedievo.get().getTpaManager().getAllPlayerIncomingTPA(player);
    List<TPA> outgoingTPA = ElMedievo.get().getTpaManager().getAllPlayerOutgoingTPA(player);

    player.sendMessage(
        Chat.buildHeader(ChatConstant.TPA_MAP.getTranslatedTo(player.getLocale()))
            + Chat.NEW_LINE
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "Incoming TPA: "
            + Chat.NEW_LINE
            + Chat.NEW_LINE
            + formattedTPAs(incomingTPA, player)
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "Outgoing TPA: "
            + Chat.NEW_LINE
            + Chat.NEW_LINE
            + formattedTPAs(outgoingTPA, player));
  }

  // TODO: Translations
  private static String formattedTPAs(List<TPA> tpas, Player player) {
    if (tpas.isEmpty())
      return ChatColor.RED
          + ChatConstant.NO_TPAS.getTranslatedTo(player.getLocale())
          + Chat.NEW_LINE;

    StringBuilder incomingMapBuilder = new StringBuilder();
    int index = 1;

    for (TPA tpa : tpas) {
      if (index == 8) break;

      final String indexedTPA = indexedTPA(tpa, index);
      incomingMapBuilder.append(indexedTPA);

      index++;
    }

    return incomingMapBuilder.toString();
  }

  private static String indexedTPA(TPA tpa, int index) {
    return ChatColor.AQUA.toString()
        + "     "
        + index
        + ". "
        + tpa.getSender().getDisplayName()
        + ChatColor.WHITE
        + " ➔ "
        + tpa.getPlayerTo().getDisplayName()
        + Chat.NEW_LINE;
  }
}
