package cl.bgmp.lobby.Portals;

/** The different types of {@link BungeePortal}s */
public enum PortalType {
  /**
   * A sign portal, which essentially is a physical sign that is bound to a bungee server. Punching
   * them will send players to it.
   */
  SIGN("sign"),

  /** A humanoid NPC which is bound to a bungee server. Punching them will send players to it. */
  NPC("npc"),

  /** Represents the lack of a type. */
  UNKNOWN("unknown");

  private String id;

  /**
   * PortalType constructor
   *
   * @param id Unique identifier for each portal used to help parsing them from config
   */
  PortalType(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
