package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.MultiResolver;
import cl.bgmp.bungee.NetworkInfoProvider;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
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
  private final CommonsBungee commonsBungee;
  private final NetworkInfoProvider networkInfoProvider;
  private final MultiResolver multiResolver;

  public ServersCommand(
      CommonsBungee commonsBungee,
      NetworkInfoProvider networkInfoProvider,
      MultiResolver multiResolver) {
    this.commonsBungee = commonsBungee;
    this.networkInfoProvider = networkInfoProvider;
    this.multiResolver = multiResolver;
  }

  @Command(
      aliases = {"servers"},
      desc = "List all available servers.",
      max = 0)
  @CommandScopes("player")
  public void servers(final CommandContext args, CommandSender sender) {
    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();

    if (servers.isEmpty()
        || servers.values().stream()
            .filter(serverInfo -> serverInfo.canAccess(sender))
            .collect(Collectors.toSet())
            .isEmpty()) {
      sender.sendMessage(this.emptyServerList(getHeader(servers)));
      return;
    }

    sender.sendMessage(this.bundledServerList(getHeader(servers), getServerList(player)));
  }

  private BaseComponent[] bundledServerList(BaseComponent[] header, BaseComponent[] list) {
    return new ComponentBuilder()
        .append(header, ComponentBuilder.FormatRetention.NONE)
        .append(list, ComponentBuilder.FormatRetention.NONE)
        .create();
  }

  private BaseComponent[] emptyServerList(BaseComponent[] header) {
    return new ComponentBuilder()
        .append(header)
        .append("No online servers :(", ComponentBuilder.FormatRetention.NONE)
        .color(ChatColor.RED)
        .create();
  }

  private BaseComponent[] getHeader(Map<String, ServerInfo> servers) {
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
                            this.networkInfoProvider.getServerMaxPlayers(serverInfo) != -1)
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

  private BaseComponent[] getServerList(ProxiedPlayer sender) {
    ComponentBuilder serverList = new ComponentBuilder();

    for (ServerInfo server : this.commonsBungee.getProxy().getServers().values()) {
      if (!server.canAccess(sender)) continue;

      final int serverMaxPlayers = this.networkInfoProvider.getServerMaxPlayers(server);
      if (serverMaxPlayers == -1) continue;

      serverList
          .append("\n")
          .append("[")
          .color(ChatColor.WHITE)
          .append(
              this.multiResolver.getClickableNameOf(server).build(),
              ComponentBuilder.FormatRetention.NONE)
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
