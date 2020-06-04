package cl.bgmp.bungee;

public enum Permission {
  HELPOP_SEE("commons.bungee.helpop.see");

  private String node;

  Permission(String node) {
    this.node = node;
  }

  public String getNode() {
    return node;
  }
}
