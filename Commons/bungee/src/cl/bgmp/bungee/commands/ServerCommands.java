package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.BungeeMessages;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.Util;
import com.sk89q.minecraft.util.commands.*;
import java.net.InetSocketAddress;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Only minor tweaks for organising {@link ServerCommands} have been made. Credit goes to the
 * original resource listed beneath
 *
 * @author https://github.com/applenick/BungeeUtils
 */
public class ServerCommands {

  @Command(
      aliases = {"hub", "lobby", "main"},
      desc = "Teleport to the lobby",
      max = 0)
  public static void hub(final CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          BungeeMessages.colourify(ChatColor.RED, ChatConstant.NO_CONSOLE.getAsTextComponent()));
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final ServerInfo suitableLobby = Util.resolveSuitableLobby();

    if (suitableLobby == null) {
      player.sendMessage(
          BungeeMessages.colourify(
              ChatColor.RED, ChatConstant.NO_LOBBIES_AVAILABLE.getAsTextComponent()));
    } else {
      player.sendMessage(
          BungeeMessages.append(
              Util.resolveServerName(suitableLobby),
              ChatColor.DARK_PURPLE + ChatConstant.TELEPORTING.getAsString()));
      player.connect(suitableLobby);
    }
  }
}
