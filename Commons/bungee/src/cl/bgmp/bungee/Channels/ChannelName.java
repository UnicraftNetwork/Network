package cl.bgmp.bungee.Channels;

public enum ChannelName {
  EVERYONE("everyone"),
  STAFF("admin");

  private String literal;

  ChannelName(String literal) {
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }
}
