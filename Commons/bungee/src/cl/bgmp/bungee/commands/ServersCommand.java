package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Util;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import java.util.Map;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServersCommand {

  @Command(
      aliases = {"servers"},
      desc = "List all available servers.",
      max = 0)
  public static void servers(final CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          new FlashComponent(ChatConstant.NO_CONSOLE.getAsString()).color(ChatColor.RED).build());
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();

    if (servers.isEmpty()
        || servers.values().stream()
            .filter(serverInfo -> serverInfo.canAccess(sender))
            .collect(Collectors.toSet())
            .isEmpty()) {
      sender.sendMessage(emptyServerList(getHeader(servers)));
      return;
    }

    sender.sendMessage(bundledServerList(getHeader(servers), getServerList(player)));
  }

  private static BaseComponent[] bundledServerList(BaseComponent[] header, BaseComponent[] list) {
    return new ComponentBuilder()
        .append(header, ComponentBuilder.FormatRetention.NONE)
        .append(list, ComponentBuilder.FormatRetention.NONE)
        .create();
  }

  private static BaseComponent[] emptyServerList(BaseComponent[] header) {
    return new ComponentBuilder()
        .append(header)
        .append("No online servers :(", ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.RED)
        .create();
  }

  private static BaseComponent[] getHeader(Map<String, ServerInfo> servers) {
    return new ComponentBuilder("-------------")
        .color(ChatColor.BLUE)
        .bold(true)
        .strikethrough(true)
        .append(" Online Servers (", ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.WHITE)
        .append(
            String.valueOf(
                servers.values().stream()
                    .filter(
                        serverInfo ->
                            CommonsBungee.get()
                                    .getNetworkInfoProvider()
                                    .getServerMaxPlayers(serverInfo)
                                != -1)
                    .collect(Collectors.toSet())
                    .size()),
            ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.GOLD)
        .append(") ", ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.WHITE)
        .append("-------------", ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.BLUE)
        .bold(true)
        .strikethrough(true)
        .create();
  }

  private static BaseComponent[] getServerList(ProxiedPlayer sender) {
    ComponentBuilder serverList = new ComponentBuilder();

    for (ServerInfo server : CommonsBungee.get().getProxy().getServers().values()) {
      if (!server.canAccess(sender)) continue;

      final int serverMaxPlayers =
          CommonsBungee.get().getNetworkInfoProvider().getServerMaxPlayers(server);
      if (serverMaxPlayers == -1) continue;

      serverList
          .append("\n")
          .append("[")
          .color(ChatColor.WHITE)
          .append(Util.resolveServerName(server).build(), ComponentBuilder.FormatRetention.NONE)
          .append("] ", ComponentBuilder.FormatRetention.NONE)
          .color(ChatColor.WHITE)
          .append("Players: ", ComponentBuilder.FormatRetention.NONE)
          .color(ChatColor.AQUA)
          .append(String.valueOf(server.getPlayers().size()), ComponentBuilder.FormatRetention.NONE)
          .color(ChatColor.GREEN)
          .append("/", ComponentBuilder.FormatRetention.NONE)
          .color(ChatColor.DARK_GRAY)
          .append(String.valueOf(serverMaxPlayers))
          .color(ChatColor.GRAY);
    }

    return serverList.create();
  }
}
