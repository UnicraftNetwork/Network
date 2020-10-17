package cl.bgmp.commons.modules;

import cl.bgmp.api.users.user.LinkedUser;
import cl.bgmp.api.users.user.User;
import cl.bgmp.butils.bungee.event.BukkitServerSwitchEvent;
import cl.bgmp.butils.chat.Chat;
import cl.bgmp.commons.ComponentWrapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerSwitchAnnouncerModule extends Module {

  public ServerSwitchAnnouncerModule() {
    super(ModuleId.SERVER_SWITCH);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    List<Player> friends = this.getFriendsOf(player);

    Optional<Module> cfmo = this.commons.getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    if (!cfmo.isPresent()) return;

    ChatFormatModule cfm = (ChatFormatModule) cfmo.get();
    BaseComponent[] joinMessage =
        new ComponentWrapper("[")
            .color(ChatColor.WHITE)
            .append(this.getClickableNameOf(this.commons.getBungee().getThisServer()))
            .append("] ")
            .color(ChatColor.WHITE)
            .append(Chat.color(cfm.getPlayerPrefix(player)))
            .append(player.getName())
            .color(ChatColor.AQUA)
            .append(" joined the game")
            .color(ChatColor.YELLOW)
            .build();

    player.sendMessage(joinMessage);
    if (friends.isEmpty()) return;

    for (Player friend : friends) {
      friend.sendMessage(joinMessage);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerSwitchServer(BukkitServerSwitchEvent event) {
    Player player = event.getPlayer();
    List<Player> friends = this.getFriendsOf(player);

    Optional<Module> cfmo = this.commons.getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    if (!cfmo.isPresent()) return;

    ChatFormatModule cfm = (ChatFormatModule) cfmo.get();
    BaseComponent[] changeServersMessage =
        new ComponentWrapper("[")
            .color(ChatColor.WHITE)
            .append(event.getFrom())
            .append(" ")
            .append(Chat.ARROWS_RIGHT)
            .append(" ")
            .append(event.getTo())
            .append("] ")
            .color(ChatColor.WHITE)
            .append(Chat.color(cfm.getPlayerPrefix(player)))
            .append(player.getName())
            .append(" changed servers")
            .build();

    player.sendMessage(changeServersMessage);
    if (friends.isEmpty()) return;

    for (Player friend : friends) {
      friend.sendMessage(changeServersMessage);
    }
  }

  private List<Player> getFriendsOf(Player player) {
    final Optional<User> user = this.api.getUserStore().getUserOf(player);
    if (!user.isPresent()) return Collections.emptyList();
    if (!(user.get() instanceof LinkedUser)) return Collections.emptyList();

    LinkedUser linkedUser = (LinkedUser) user.get();

    return linkedUser.getFriends().stream()
        .filter(f -> this.commons.getServer().getPlayer(f) != null)
        .map(f -> this.commons.getServer().getPlayer(f))
        .collect(Collectors.toList());
  }

  private BaseComponent[] getClickableNameOf(String server) {
    server = server == null ? "Unknown" : server;

    return new ComponentWrapper(server)
        .color(ChatColor.GOLD)
        .hoverText(
            new ComponentWrapper("[")
                .color(ChatColor.WHITE)
                .append(server)
                .color(ChatColor.GOLD)
                .append("] ")
                .color(ChatColor.WHITE)
                .append("Click to connect")
                .color(ChatColor.GREEN)
                .build())
        .clickCommand("server " + server)
        .build();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
