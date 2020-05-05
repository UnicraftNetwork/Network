package cl.bgmp.elmedievo.commands.TPA;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.teleport.TPA;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.textcomponents.ComponentFactory;
import cl.bgmp.utilsbukkit.textcomponents.ComponentSender;
import cl.bgmp.utilsbukkit.textcomponents.ComponentWizard;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TPAList {
  @Command(
      aliases = {"tplist", "tpalist", "tpamap"},
      desc = "Lists all incoming and outgoing teleport requests.",
      max = 0)
  public static void tpalist(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + Translations.get("commands.no.console", sender));
      return;
    }

    Player player = (Player) sender;
    List<TPA> incomingTPA = ElMedievo.get().getTpaManager().getAllPlayerIncomingTPA(player);
    List<TPA> outgoingTPA = ElMedievo.get().getTpaManager().getAllPlayerOutgoingTPA(player);

    final TextComponent header =
        new ComponentWizard(Chat.buildHeader(Translations.get("tpa.map", sender)))
            .getTextComponent();
    final TextComponent first =
        new ComponentWizard(
                Chat.NEW_LINE + ChatColor.GOLD + " » " + ChatColor.GREEN + "Incoming TPA: ")
            .getTextComponent();
    final TextComponent second =
        new ComponentWizard(ChatColor.GOLD + " » " + ChatColor.GREEN + "Outgoing TPA: ")
            .getTextComponent();

    ComponentSender.sendComponent(ComponentFactory.squashComponents(header, first), player);
    ComponentSender.sendComponent(formattedTPAs(incomingTPA, player), player);
    ComponentSender.sendComponent(second, player);
    ComponentSender.sendComponent(formattedTPAs(outgoingTPA, player), player);
  }

  private static BaseComponent[] formattedTPAs(List<TPA> tpas, Player player) {
    if (tpas.isEmpty())
      return new ComponentWizard(ChatColor.RED + Translations.get("tpa.no.tpas", player))
          .asBaseComponentArray();

    ComponentBuilder componentBuilder = new ComponentBuilder();
    int index = 1;

    for (TPA tpa : tpas) {
      // Pagination should make this limit of 8 go away
      // as for now, the maximum amount of displayed TPAs will be 8
      if (index == 8) break;

      final TextComponent indexedTPARelation =
          new ComponentWizard(
                  ChatColor.AQUA.toString()
                      + "     "
                      + index
                      + ". "
                      + ChatColor.RESET
                      + tpa.getSender().getDisplayName()
                      + ChatColor.WHITE
                      + " ➔ "
                      + tpa.getPlayerTo().getDisplayName()
                      + " ")
              .getTextComponent();

      final TextComponent acceptClickable;
      if (tpa.getPlayerTo().equals(player)) {
        acceptClickable =
            new ComponentWizard(ChatColor.GREEN + Translations.get("tpa.click.to.accept", player))
                .setHoverable(Translations.get("tpa.click.to.accept", player))
                .setClickable("/tpaccept " + tpa.getSender().getName())
                .getTextComponent();
      } else acceptClickable = new ComponentWizard("").getTextComponent();

      componentBuilder.append(
          ComponentFactory.squashComponents(indexedTPARelation, acceptClickable));
      index++;
    }

    return componentBuilder.create();
  }
}
