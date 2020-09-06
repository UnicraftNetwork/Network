package cl.bgmp.bungee.channels;

/** A chat {@link Channel} enum class for their names */
public enum ChannelName {
  EVERYONE("everyone"),
  STAFF("admin"),
  REF("referee"),
  EC("eventcoordinator");

  private String literal;

  ChannelName(String literal) {
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }
}
