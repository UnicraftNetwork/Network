package cl.bgmp.elmedievo.Translations.Languages;

import cl.bgmp.elmedievo.Translations.ChatConstant;

public enum ES_CL {
  // Comandos
  NO_PERMISSION(ChatConstant.NO_PERMISSION, "No tienes permiso mierda!"),
  TOO_FEW_ARGS(ChatConstant.TOO_FEW_ARGS, "Muy pocos argumentos."),
  TOO_MANY_ARGS(ChatConstant.TOO_MANY_ARGS, "Demasiados argumentos."),
  NO_CONSOLE(ChatConstant.NO_CONSOLE, "Tienes que ser un jugador para ejecutar este comando!"),
  NO_PLAYER(
      ChatConstant.NO_PLAYER, "Tienes que ejecutar este comando via la consola del servidor!"),
  INVALID_PLAYER(ChatConstant.INVALID_PLAYER, "Jugador no encontrado."),
  NUMBER_STRING_EXCEPTION(ChatConstant.NUMBER_STRING_EXCEPTION, "Tienes que esperar un número."),
  UNKNOWN_ERROR(ChatConstant.UNKNOWN_ERROR, "Ha ocurrido un error desconocido."),
  SPAWN_TELEPORTED(ChatConstant.SPAWN_TELEPORTED, "Has sido teletransportado al spawn."),

  // TPA
  ALREADY_REQUESTING_TPA(
      ChatConstant.ALREADY_REQUESTING_TPA, "Ya tienes una solicitud enviada a ese jugador!."),
  TPA_ACCEPT(ChatConstant.TPA_ACCEPT, "Aceptaste el tpa de {0}!"),
  TPA_SENT(ChatConstant.TPA_SENT, "Solicitud de tpa enviada a {0}"),
  TPA_NO_PENDANT(ChatConstant.TPA_NO_PENDANT, "No tienes ninguna solicitud de tpa pendiente."),
  TPA_ACCEPTED(ChatConstant.TPA_ACCEPTED, "Tu solicitud de tpa ha sido aceptada por {0}!"),
  TPA_DENY(ChatConstant.TPA_DENY, "Has rechazado la solicitud de tpa que te envió {0}."),
  TPA_DENIED(ChatConstant.TPA_DENIED, "{0} ha rechazado la solicitud de tpa que le enviaste."),
  TPA_CANCEL(ChatConstant.TPA_CANCEL, "Cancelaste la solicitud de tpa enviada a {0}."),
  TPA_CANCELLED(ChatConstant.TPA_CANCELLED, "La solicitud de tpa entrante fue cancelada por {0}."),
  TPA_EXPIRED(ChatConstant.TPA_EXPIRED, "Tu solicitud de tpa enviada a {0} expiró."),
  TPA_RECEIVED(
      ChatConstant.TPA_RECEIVED,
      "{0} &ate envió una solicitud de tpa.-|&a➥ Usa &b/tpaccept para aceptarla &ao &c/tpdeny para rechazarla&a.-|&aEsta solicitud expirará en &4120 &asegundos."),
  TPA_CANCELLED_ALL(
      ChatConstant.TPA_CANCELLED_ALL, "Se cancelaron todas las solicitudes de tpa entrantes."),
  NO_TPAS(ChatConstant.NO_TPAS, "No tienes solicitudes de tpa pendientes."),
  ACCEPT_BUTTON(ChatConstant.ACCEPT_BUTTON, "Aceptar"),
  ACCEPT_BUTTON_HOVER(ChatConstant.ACCEPT_BUTTON_HOVER, "Click para aceptar."),
  TPA_MAP(ChatConstant.TPA_MAP, "Mapa tpa"),

  // Misc
  RELOADED_FURNACE_RECIPES(
      ChatConstant.RELOADED_FURNACE_RECIPES, "Se recargaron todas las recetas del horno.");

  private ChatConstant chatConstant;
  private String translation;

  ES_CL(ChatConstant chatConstant, String translation) {
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
