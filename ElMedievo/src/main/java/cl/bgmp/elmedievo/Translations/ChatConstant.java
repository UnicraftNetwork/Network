package cl.bgmp.elmedievo.Translations;

public enum ChatConstant {
  // Commands
  NO_PERMISSION("You do not have permission!"),
  TOO_FEW_ARGS("Too few arguments."),
  TOO_MANY_ARGS("Too many arguments."),
  NO_CONSOLE("You must be a player to execute this command!"),
  NO_PLAYER("You must execute this command via server console!"),

  // TPA
  @MissingTranslation
  TPA_SENT("Teleport request successfully sent to {0}."),
  ALREADY_REQUESTING_TPA("You are already requesting to teleport to this player."),
  @MissingTranslation
  TPA_NO_PENDANT("You do not have any incoming tpa."),
  TPA_ACCEPT("Successfully accepted {0}'s teleport request."),
  @MissingTranslation
  TPA_ACCEPTED("Your teleport request was accepted by {0}!"),
  @MissingTranslation
  TPA_DENY("You have denied an incoming teleport request from {0}."),
  @MissingTranslation
  TPA_DENIED("{0} has denied your teleport request to them."),
  @MissingTranslation
  TPA_CANCEL("Successfully cancelled outgoing teleport request to {0}."),
  @MissingTranslation
  TPA_CANCELLED("Incoming teleport request was cancelled by {0}."),

  @MissingTranslation
  TPA_EXPIRED("Your teleport request to {0} has expired."),
  @MissingTranslation
  TPA_RECEIVED(
      "{0} &ais requesting to teleport to you.-|&a➥ Use &b/tpaccept &aor &c/tpdeny&a.-|&aThis request will expire in &4120 &aseconds."),
  @MissingTranslation
  RELOADED_FURNACE_RECIPES("Successfully reloaded all furnace recipes."),
  @MissingTranslation
  TPA_CANCELLED_ALL("Cancelled all outgoing teleport requests."),
  @MissingTranslation
  NO_TPAS("No Teleport requests to display."),
  @MissingTranslation
  TPA_MAP("TPA Map"),
  @MissingTranslation
  ACCEPT_BUTTON("Accept"),
  @MissingTranslation
  ACCEPT_BUTTON_HOVER("Click to accept"),

  // Misc
  @MissingTranslation
  NUMBER_STRING_EXCEPTION("Expected a number, string received instead."),
  @MissingTranslation
  UNKNOWN_ERROR("An unknown error has occurred."),
  INVALID_PLAYER("No players matched query."),
  @MissingTranslation
  SPAWN_TELEPORTED("You have been teleported to the spawn.");

  private String string;

  ChatConstant(String string) {
    this.string = string;
  }

  public String getString() {
    return string;
  }

  public String getTranslatedTo(String locale) {
    return Translator.translate(locale, this);
  }
}
