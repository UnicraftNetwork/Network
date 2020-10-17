package cl.bgmp.bungee.users.user;

import java.util.UUID;

/** Represents a Network user. Extended by {@link LinkedUser} and {@link UnlinkedUser} */
public abstract class User {
  protected final UUID uuid;
  protected final String nick;

  public User(UUID uuid, String nick) {
    this.uuid = uuid;
    this.nick = nick;
  }

  public UUID getUUID() {
    return uuid;
  }

  public String getNick() {
    return nick;
  }
}
