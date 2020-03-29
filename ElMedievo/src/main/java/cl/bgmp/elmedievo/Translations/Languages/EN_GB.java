package cl.bgmp.elmedievo.Translations.Languages;

import cl.bgmp.elmedievo.Translations.ChatConstant;

public enum EN_GB {
  NO_PERMISSION(ChatConstant.NO_PERMISSION, "You do not have permission!"),
  TOO_FEW_ARGS(ChatConstant.TOO_FEW_ARGS, "Too few arguments."),
  TOO_MANY_ARGS(ChatConstant.TOO_MANY_ARGS, "Too many arguments."),
  NO_CONSOLE(ChatConstant.NO_CONSOLE, "You must be a player to execute this command!"),
  NO_PLAYER(ChatConstant.NO_PLAYER, "You must execute this command via server console!"),
  INVALID_PLAYER(ChatConstant.INVALID_PLAYER, "No players matched query."),
  ALREADY_REQUESTING_TPA(
      ChatConstant.ALREADY_REQUESTING_TPA,
      "You are already requesting to teleport to this player."),
  TPA_ACCEPT(ChatConstant.TPA_ACCEPT, "Successfully accepted {0}'s teleport request.");

  private ChatConstant chatConstant;
  private String translation;

  EN_GB(ChatConstant chatConstant, String translation) {
    this.chatConstant = chatConstant;
    this.translation = translation;
  }

  public ChatConstant getChatConstant() {
    return chatConstant;
  }

  public String getTranslation() {
    return translation;
  }
}
