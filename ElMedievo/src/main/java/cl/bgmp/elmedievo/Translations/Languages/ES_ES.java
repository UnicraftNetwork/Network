package cl.bgmp.elmedievo.Translations.Languages;

import cl.bgmp.elmedievo.Translations.ChatConstant;

public enum ES_ES {
  NO_PERMISSION(ChatConstant.NO_PERMISSION, "No tienes permiso!"),
  TOO_FEW_ARGS(ChatConstant.TOO_FEW_ARGS, "Muy pocos argumentos."),
  TOO_MANY_ARGS(ChatConstant.TOO_MANY_ARGS, "Demasiados argumentos."),
  NO_CONSOLE(ChatConstant.NO_CONSOLE, "Debes ser un jugador para ejecutar este comando!"),
  NO_PLAYER(ChatConstant.NO_PLAYER, "Debes ejecutar este comando via la consola del servidor!"),
  INVALID_PLAYER(ChatConstant.INVALID_PLAYER, "Jugador no encontrado."),
  ALREADY_REQUESTING_TPA(
      ChatConstant.ALREADY_REQUESTING_TPA, "Ya tienes una solicitud enviada a ese jugador!."),
  TPA_ACCEPT(ChatConstant.TPA_ACCEPT, "Has aceptado el tpa de {0}.");

  private ChatConstant chatConstant;
  private String translation;

  ES_ES(ChatConstant chatConstant, String translation) {
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
