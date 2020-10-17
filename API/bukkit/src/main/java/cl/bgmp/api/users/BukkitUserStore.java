package cl.bgmp.api.users;

import cl.bgmp.api.APIBukkit;
import cl.bgmp.api.Config;
import cl.bgmp.api.users.user.LinkedUser;
import cl.bgmp.api.users.user.UnlinkedUser;
import cl.bgmp.api.users.user.User;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitUserStore implements Listener {
  private final List<User> users = new ArrayList<>();

  private final APIBukkit api;
  private final Config config;
  private final MySQLConnector database;

  @Inject
  public BukkitUserStore(APIBukkit api) {
    this.api = api;
    this.config = api.getConfiguration();
    this.database = api.getDatabase();

    this.api.getServer().getPluginManager().registerEvents(this, this.api);
  }

  public Optional<User> getUserOf(Player player) {
    return this.users.stream().filter(u -> u.getNick().equals(player.getName())).findFirst();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
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
        new LinkedUser(
            playerUUID.get(), player.getName(), this.getFriendsOf(player.getUniqueId())));
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    final Optional<User> user = this.getUserOf(player);
    if (!user.isPresent()) return;

    this.users.remove(user.get());
  }

  private boolean isPlayerLinked(Player player) {
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

  private Optional<UUID> getPlayerUUID(Player player) {
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
