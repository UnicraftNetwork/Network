package cl.bgmp.bungee.listeners;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import cl.bgmp.bungee.users.user.LinkedUser;
import cl.bgmp.bungee.users.user.User;
import cl.bgmp.butils.chat.Chat;
import com.google.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerSwitchServer implements Listener {

  private final CommonsBungee commonsBungee;
  private final MultiResolver multiResolver;
  private final APIBungee api;

  @Inject
  public PlayerSwitchServer(
      CommonsBungee commonsBungee, MultiResolver multiResolver, APIBungee api) {
    this.commonsBungee = commonsBungee;
    this.multiResolver = multiResolver;
    this.api = api;
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerSwitchServer(ServerSwitchEvent event) {
    ProxiedPlayer player = event.getPlayer();
    Optional<User> user = this.api.getUserStore().getUserOf(player);
    if (!user.isPresent()) return;
    if (!(user.get() instanceof LinkedUser)) return;

    LinkedUser linkedUser = (LinkedUser) user.get();
    List<UUID> storedFriends = linkedUser.getFriends();
    if (storedFriends == null) return;

    ServerInfo from = event.getFrom();
    ServerInfo to = event.getPlayer().getServer().getInfo();

    List<ProxiedPlayer> friends =
        storedFriends.stream()
            .filter(f -> this.commonsBungee.getProxy().getPlayer(f) != null)
            .map(f -> this.commonsBungee.getProxy().getPlayer(f))
            .filter(f -> !to.getPlayers().contains(f))
            .collect(Collectors.toList());

    BaseComponent[] message = this.resolveMessage(from, to, player);
    friends.forEach(f -> f.sendMessage(message));
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    final ProxiedPlayer player = event.getPlayer();
    final Optional<User> user = this.api.getUserStore().getUserOf(player);
    if (!user.isPresent()) return;
    if (!(user.get() instanceof LinkedUser)) return;

    final LinkedUser linkedUser = (LinkedUser) user.get();
    final List<UUID> storedFriends = linkedUser.getFriends();
    if (storedFriends == null) return;

    ServerInfo from = event.getPlayer().getServer().getInfo();

    BaseComponent[] leaveMessage =
        new ComponentWrapper("[")
            .color(ChatColor.WHITE)
            .append(
                this.multiResolver
                    .getClickableNameOf(player.getServer())
                    .append(player.getName())
                    .color(ChatColor.DARK_AQUA)
                    .append(" " + ChatConstant.LEFT_THE_GAME.getAsString())
                    .color(ChatColor.YELLOW))
            .build();

    storedFriends.stream()
        .filter(f -> this.commonsBungee.getProxy().getPlayer(f) != null)
        .map(f -> this.commonsBungee.getProxy().getPlayer(f))
        .filter(f -> !from.getPlayers().contains(f))
        .forEach(f -> f.sendMessage(leaveMessage));
  }

  private BaseComponent[] resolveMessage(ServerInfo from, ServerInfo to, ProxiedPlayer player) {
    BaseComponent[] prefix;
    String action;

    if (from == null) {
      prefix =
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(this.multiResolver.getClickableNameOf(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .build();
      action = ChatConstant.JOINED_THE_GAME.getAsString();
    } else {
      prefix =
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(this.multiResolver.getClickableNameOf(from))
              .append(" ")
              .append(Chat.ARROWS_RIGHT)
              .append(" ")
              .append(this.multiResolver.getClickableNameOf(to))
              .append("] ")
              .color(ChatColor.WHITE)
              .build();
      action = ChatConstant.CHANGED_SERVERS.getAsString();
    }

    return new ComponentWrapper()
        .append(prefix)
        .append(player.getDisplayName())
        .color(ChatColor.DARK_AQUA)
        .append(" " + action)
        .color(ChatColor.YELLOW)
        .build();
  }
}
