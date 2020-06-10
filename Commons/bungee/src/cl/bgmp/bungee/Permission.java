package cl.bgmp.bungee;

public enum Permission {
  HELPOP_SEE("commons.bungee.helpop.see"),
  STAFF_CHAT_SEE("commons.bungee.staffchat.see");

  private final String node;

  Permission(final String node) {
    this.node = node;
  }

  public String getNode() {
    return node;
  }
}
