package cl.bgmp.elmedievo.Translations.Languages;

import cl.bgmp.elmedievo.Translations.ChatConstant;

public enum LOL_US {
  NO_PERMISSION(ChatConstant.NO_PERMISSION, "kitten has no perms."),
  TOO_FEW_ARGS(ChatConstant.TOO_FEW_ARGS, "too few wurds."),
  TOO_MANY_ARGS(ChatConstant.TOO_MANY_ARGS, "comamd to loooooong."),
  NO_CONSOLE(ChatConstant.NO_CONSOLE, "u must b a human!"),
  NO_PLAYER(ChatConstant.NO_PLAYER, "u must b a servur!"),
  INVALID_PLAYER(ChatConstant.INVALID_PLAYER, "kat not fund."),
  ALREADY_REQUESTING_TPA(
      ChatConstant.ALREADY_REQUESTING_TPA, "Alredy rekqesting teleportationn to that kitteh."),
  TPA_ACCEPT(ChatConstant.TPA_ACCEPT, "gudly acceqted {0}'s kitteh teletransportation reqqst.");

  private ChatConstant chatConstant;
  private String translation;

  LOL_US(ChatConstant chatConstant, String translation) {
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
