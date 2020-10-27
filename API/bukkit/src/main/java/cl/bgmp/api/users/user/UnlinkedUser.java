package cl.bgmp.api.users.user;

import cl.bgmp.api.APIBukkit;
import cl.bgmp.butils.mysql.Column;
import cl.bgmp.butils.mysql.MySQLSelect;
import cl.bgmp.butils.mysql.MySQLUpdate;
import cl.bgmp.butils.mysql.SQLDataType;
import java.util.Optional;
import java.util.UUID;

/** Represents a {@link User} who has NOT linked their Minecraft account with the forums. */
public class UnlinkedUser extends User {

  public UnlinkedUser(UUID uuid, String nick) {
    super(uuid, nick);
  }

  /**
   * Links this {@link User} with the forums.
   *
   * @param token The token given to the user at the website, for them to provide in-game in order
   *     to link their account.
   * @return {@code true} if the link was successful, {@code} false if anything went wrong.
   */
  public boolean link(String token) {
    Optional<String> validToken = this.lookForToken(token);
    if (!validToken.isPresent()) return false;

    new MySQLUpdate(APIBukkit.get().getDatabase())
        .atTable(APIBukkit.get().getConfiguration().getUsersTable())
        .updating(
            new Column("uuid", SQLDataType.STRING),
            new Column("minecraft_token", SQLDataType.STRING))
        .respectivelyWith(this.getUUID().toString(), null)
        .where(new Column("minecraft_token", SQLDataType.STRING))
        .is(validToken.get())
        .execute();

    return true;
  }

  private Optional<String> lookForToken(String token) {
    String validToken =
        new MySQLSelect(APIBukkit.get().getDatabase())
            .atTable(APIBukkit.get().getConfiguration().getUsersTable())
            .getting(new Column("minecraft_token", SQLDataType.STRING))
            .where(new Column("minecraft_token", SQLDataType.STRING))
            .is(token)
            .asString();

    if (validToken == null) return Optional.empty();
    else return Optional.of(validToken);
  }
}
