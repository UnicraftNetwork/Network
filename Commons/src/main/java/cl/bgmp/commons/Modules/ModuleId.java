package cl.bgmp.commons.Modules;

public enum ModuleId {
  CHAT_FORMAT("chatformat-module"),
  FORCE_GAMEMODE("forcegamemode-module"),
  NAVIGATOR("navigator-module"),
  JOIN_TOOLS("jointools-module"),
  WEATHER("weather-module");

  private String toString;

  ModuleId(String toString) {
    this.toString = toString;
  }

  @Override
  public String toString() {
    return toString;
  }
}
