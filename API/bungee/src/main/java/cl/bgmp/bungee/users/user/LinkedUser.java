package cl.bgmp.bungee.users.user;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.butils.mysql.Column;
import cl.bgmp.butils.mysql.MySQLSelect;
import cl.bgmp.butils.mysql.MySQLUpdate;
import cl.bgmp.butils.mysql.SQLDataType;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Represents a {@link User} who has linked their Minecraft account with the forums. */
public class LinkedUser extends User {
  private final LinkedList<UUID> friends; // UUIDs of friends

  public LinkedUser(UUID uuid, String nick, LinkedList<UUID> friends) {
    super(uuid, nick);
    this.friends = friends;
  }

  public List<UUID> getFriends() {
    return friends;
  }

  public Optional<String> getLocale() {
    String locale =
        new MySQLSelect(APIBungee.get().getDatabase())
            .atTable(APIBungee.get().getConfiguration().getUsersTable())
            .getting(new Column("locale", SQLDataType.STRING))
            .where(new Column("uuid", SQLDataType.STRING))
            .is(this.uuid.toString())
            .asString();

    if (locale == null) return Optional.empty();
    else return Optional.of(locale);
  }

  public void addFriend(User user) {
    this.friends.add(user.getUUID());

    new MySQLUpdate(APIBungee.get().getDatabase())
        .atTable(APIBungee.get().getConfiguration().getUsersTable())
        .updating(new Column("friends", SQLDataType.STRING))
        .respectivelyWith(this.getSerializedFriends())
        .where(new Column("uuid", SQLDataType.STRING))
        .is(this.getUUID().toString())
        .execute();
  }

  public void removeFriend(User friend) {
    this.friends.remove(friend.getUUID());

    new MySQLUpdate(APIBungee.get().getDatabase())
        .atTable(APIBungee.get().getConfiguration().getUsersTable())
        .updating(new Column("friends", SQLDataType.STRING))
        .respectivelyWith(this.friends.isEmpty() ? "" : this.getSerializedFriends())
        .where(new Column("uuid", SQLDataType.STRING))
        .is(this.getUUID().toString())
        .execute();
  }

  /**
   * Returns the string of this user's friends' UUIDs, serialized in the database format a#b#c...
   *
   * @return The serialized string of friends
   */
  private String getSerializedFriends() {
    StringBuilder builder = new StringBuilder();

    for (UUID friend : this.friends) {
      builder.append(friend).append("#");
    }

    return builder.toString();
  }
}
