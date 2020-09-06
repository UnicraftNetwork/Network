package cl.bgmp.bungee;

public enum Permission {
  HELPOP_SEE("commons.bungee.helpop.see"),
  STAFF_CHAT_SEE("commons.bungee.staffchat.see"),
  REF_CHAT_SEE("commons.bungee.referee.see"),
  EC_CHAT_SEE("commons.bungee.eventcoord.see");

  private final String node;

  Permission(final String node) {
    this.node = node;
  }

  public String getNode() {
    return node;
  }
}
