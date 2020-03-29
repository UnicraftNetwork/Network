package cl.bgmp.elmedievo.Reciper.Exceptions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RecipeXMLException extends Exception {
  private RecipeXMLExceptionCause cause;

  public RecipeXMLException(RecipeXMLExceptionCause cause) {
    this.cause = cause;
  }

  /**
   * Exception's cause in
   *
   * @return The specific exception's cause
   */
  public RecipeXMLExceptionCause getExceptionCause() {
    return cause;
  }

  /** Prompts the exception's description to the server's console */
  @Override
  public void printStackTrace() {
    Bukkit.getConsoleSender()
        .sendMessage(
            ChatColor.RED
                + "Found exception in "
                + ChatColor.GOLD
                + " furnace.xml: "
                + cause.getDescription());
  }
}
