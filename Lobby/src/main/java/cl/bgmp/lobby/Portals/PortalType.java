package cl.bgmp.lobby.Portals;

public enum PortalType {
  SIGN("sign"),
  NPC("npc"),
  UNKNOWN("unknown");

  private String id;

  PortalType(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
