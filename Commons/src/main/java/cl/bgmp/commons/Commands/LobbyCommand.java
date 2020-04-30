package cl.bgmp.commons.Commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand {
  @Command(
      aliases = {"lobby", "hub", "udec"},
      desc = "Teleports you to the server hub.",
      max = 0)
  public static void lobby(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + Translations.get("commands.no.console", sender));
      return;
    }

    sender.sendMessage();
    Channels.sendPlayerToServer(Commons.get(), (Player) sender, Config.Lobby.getLobbyServerName());
  }
}
