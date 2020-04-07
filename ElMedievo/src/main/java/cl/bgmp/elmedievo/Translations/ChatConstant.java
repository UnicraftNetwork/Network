package cl.bgmp.elmedievo.Translations;

public enum ChatConstant {
  // Commands
  NO_PERMISSION("You do not have permission!"),
  TOO_FEW_ARGS("Too few arguments."),
  TOO_MANY_ARGS("Too many arguments."),
  NO_CONSOLE("You must be a player to execute this command!"),
  NO_PLAYER("You must execute this command via server console!"),

  // TPA
  TPA_SENT("Teleport request successfully sent to {0}."),
  ALREADY_REQUESTING_TPA("You are already requesting to teleport to this player."),
  TPA_NO_PENDANT("You do not have any incoming tpa."),
  TPA_ACCEPT("Successfully accepted {0}'s teleport request."),
  TPA_ACCEPTED("Your teleport request was accepted by {0}!"),
  TPA_DENY("You have denied an incoming teleport request from {0}."),
  TPA_DENIED("{0} has denied your teleport request to them."),
  TPA_CANCEL("Successfully cancelled outgoing teleport request to {0}."),
  TPA_CANCELLED("Incoming teleport request was cancelled by {0}."),
  TPA_EXPIRED("Your teleport request to {0} has expired."),
  TPA_RECEIVED(
      "{0} &ais requesting to teleport to you.-|&a➥ Use &b/tpaccept &aor &c/tpdeny&a.-|&aThis request will expire in &4120 &aseconds."),
  @MissingTranslation
  RELOADED_FURNACE_RECIPES("Successfully reloaded all furnace recipes."),
  TPA_CANCELLED_ALL("Cancelled all outgoing teleport requests."),
  NO_TPAS("No Teleport requests to display."),
  TPA_MAP("TPA Map"),
  ACCEPT_BUTTON("Accept"),
  ACCEPT_BUTTON_HOVER("Click to accept"),

  // Misc
  NUMBER_STRING_EXCEPTION("Expected a number, string received instead."),
  UNKNOWN_ERROR("An unknown error has occurred."),
  INVALID_PLAYER("No players matched query."),
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
