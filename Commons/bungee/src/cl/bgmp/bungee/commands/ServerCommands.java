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
    final ServerInfo suitableLobby = Util.resolveSuitableLobbyForPlayer(player);

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

  @Command(
      aliases = {"addserver"},
      desc = "Add a BungeeCord server",
      usage = "<name> <address> [port]",
      flags = "r",
      min = 2,
      max = 4)
  @CommandPermissions("commons.bungee.command.addserver")
  public static void addserver(final CommandContext args, CommandSender sender)
      throws CommandException {
    String name = args.getString(0);
    String address = args.getString(1);
    int port = args.argsLength() > 2 ? args.getInteger(2) : 25565;
    boolean restricted = args.hasFlag('r');

    ServerInfo serverInfo =
        ProxyServer.getInstance()
            .constructServerInfo(name, new InetSocketAddress(address, port), "", restricted);
    ProxyServer.getInstance().getServers().put(name, serverInfo);

    sender.sendMessage(
        BungeeMessages.append(
            BungeeMessages.colourify(
                ChatColor.GREEN, ChatConstant.ADDED_SERVER.getAsTextComponent()),
            ChatColor.GOLD + name));
  }

  @Command(
      aliases = {"delserver"},
      desc = "Remove a BungeeCord server",
      usage = "<name>",
      min = 1,
      max = 1)
  @CommandPermissions("commons.bungee.command.delserver")
  public static void delserver(final CommandContext args, CommandSender sender) {
    String name = args.getString(0);

    if (ProxyServer.getInstance().getServers().remove(name) == null) {
      sender.sendMessage(
          BungeeMessages.append(
              BungeeMessages.colourify(
                  ChatColor.RED, ChatConstant.SERVER_NOT_FOUND.getAsTextComponent()),
              ChatColor.GOLD + name));
    } else {
      sender.sendMessage(
          BungeeMessages.append(
              BungeeMessages.colourify(
                  ChatColor.RED, ChatConstant.REMOVED_SERVER.getAsTextComponent()),
              ChatColor.GOLD + name));
    }
  }
}
