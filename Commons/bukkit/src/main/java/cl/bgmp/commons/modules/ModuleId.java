package cl.bgmp.commons.modules;

public enum ModuleId {
  CHAT_FORMAT("chatformat-module"),
  FORCE_GAMEMODE("forcegamemode-module"),
  NAVIGATOR("navigator-module"),
  JOIN_TOOLS("jointools-module"),
  WEATHER("weather-module"),
  JOINQUIT_MESSAGES("joinquit-messages-module"),
  RESTART("restart-module"),
  TIPS("tips-module"),
  CHAT_CENSOR("chat-censor-module"),
  SERVER_SWITCH("server-switch-module");

  private String toString;

  ModuleId(String toString) {
    this.toString = toString;
  }

  @Override
  public String toString() {
    return toString;
  }
}
