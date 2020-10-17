package cl.bgmp.bungee.users;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.Config;
import cl.bgmp.bungee.users.user.LinkedUser;
import cl.bgmp.bungee.users.user.UnlinkedUser;
import cl.bgmp.bungee.users.user.User;
import cl.bgmp.butils.mysql.Column;
import cl.bgmp.butils.mysql.MySQLConnector;
import cl.bgmp.butils.mysql.MySQLSelect;
import cl.bgmp.butils.mysql.SQLDataType;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeUserStore implements Listener {
  private final List<User> users = new ArrayList<>();

  private final APIBungee api;
  private final Config config;
  private final MySQLConnector database;

  @Inject
  public BungeeUserStore(APIBungee api) {
    this.api = api;
    this.config = api.getConfiguration();
    this.database = api.getDatabase();

    this.api.getProxy().getPluginManager().registerListener(this.api, this);
  }

  public Optional<User> getUserOf(ProxiedPlayer player) {
    return this.users.stream().filter(u -> u.getNick().equals(player.getName())).findFirst();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(ServerSwitchEvent event) {
    final ServerInfo from = event.getFrom();
    if (from != null) return;

    final ProxiedPlayer player = event.getPlayer();
    final Optional<UUID> playerUUID = this.getPlayerUUID(player);
    if (!playerUUID.isPresent()) {
      this.api.getLogger().severe("Could not load profile for " + player.getName());
      return;
    }

    if (!this.isPlayerLinked(player)) {
      this.users.add(new UnlinkedUser(playerUUID.get(), player.getName()));
      return;
    }

    this.users.add(
        new LinkedUser(playerUUID.get(), player.getName(), this.getFriendsOf(playerUUID.get())));
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerQuit(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();
    final Optional<User> user = this.getUserOf(player);
    if (!user.isPresent()) return;

    this.users.remove(user.get());
  }

  public Optional<UUID> getPlayerUUID(ProxiedPlayer player) {
    String uuid =
        new MySQLSelect()
            .from(database.getConnection())
            .atTable(this.config.getMinecraftUsersTable())
            .getting(new Column("uuid", SQLDataType.STRING))
            .where(new Column("nick", SQLDataType.STRING))
            .is(player.getName())
            .asString();

    if (uuid == null) return Optional.empty();
    else return Optional.of(UUID.fromString(uuid));
  }

  private boolean isPlayerLinked(ProxiedPlayer player) {
    String uuid =
        new MySQLSelect()
            .from(database.getConnection())
            .atTable(this.config.getUsersTable())
            .getting(new Column("uuid", SQLDataType.STRING))
            .where(new Column("uuid", SQLDataType.STRING))
            .is(player.getUniqueId().toString())
            .asString();

    return uuid != null;
  }

  private LinkedList<UUID> getFriendsOf(UUID user) {
    String serializedFriends =
        new MySQLSelect()
            .from(database.getConnection())
            .atTable(this.config.getUsersTable())
            .getting(new Column("friends", SQLDataType.STRING))
            .where(new Column("uuid", SQLDataType.STRING))
            .is(user.toString())
            .asString();

    if (serializedFriends.equals("")) return new LinkedList<>();

    return Arrays.stream(serializedFriends.split("#"))
        .map(UUID::fromString)
        .collect(Collectors.toCollection(LinkedList::new));
  }
}
