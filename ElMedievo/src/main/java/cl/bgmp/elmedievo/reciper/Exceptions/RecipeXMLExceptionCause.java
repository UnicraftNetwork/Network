package cl.bgmp.elmedievo.reciper.Exceptions;

import org.bukkit.ChatColor;

public enum RecipeXMLExceptionCause {
  /** Recipe parsing exception clauses */
  MISSING_ID_ATTRIBUTE("Missing required attribute: 'id'."),
  INVALID_ID("Invalid id."),
  DUPLICATED_ID("Two recipes share the same id! They must be unique!"),
  MISSING_EXPERIENCE_ATTRIBUTE("Missing required attribute: 'experience'."),
  INVALID_EXPERIENCE_LEVEL("Invalid experience level."),
  MISSING_COOKING_TIME_ATTRIBUTE("Missing required attribute: 'time'."),
  INVALID_COOKING_TIME("Invalid cooking time."),
  MISSING_REQUIRED_CHILD_SOURCE("Missing required child element '<source/>'."),
  NO_SOURCE_EXCEPTION("The <recipe/> must contain a source within the <source> </source> tag."),
  INVALID_SOURCE_MATERIAL("The material contained in <source> </source> must be a valid material."),
  MISSING_REQUIRED_CHILD_RESULT("Missing required child element '<result/>'."),
  NO_RESULT_EXCEPTION("The <recipe/> must contain a result within the <result> </result> tag."),
  INVALID_RESULT_MATERIAL("The material contained in <result> </result> must be a valid material."),
  INVALID_RESULT_AMOUNT("Invalid result amount.");

  private String description;

  /**
   * Recipe parsing exception constructor
   *
   * @param description Information on the exception detail
   */
  RecipeXMLExceptionCause(String description) {
    this.description = description;
  }

  /**
   * Beautifies and returns a detailed description message for the exception clause
   *
   * @return Description message
   */
  public String getDescription() {
    return ChatColor.RED
        + description.replaceAll("<", ChatColor.AQUA + "<").replaceAll(">", ">" + ChatColor.RED)
        + ChatColor.RESET;
  }
}
