package cl.bgmp.elmedievo.reciper.Furnace;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.reciper.Exceptions.RecipeXMLException;
import cl.bgmp.elmedievo.reciper.Exceptions.RecipeXMLExceptionCause;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class FurnaceRecipesManager {
  private Document furnaceXML;
  private List<CustomFurnaceRecipe> recipes = new ArrayList<>();

  public FurnaceRecipesManager() {
    load();
  }

  private void load() {
    File furnaceXMLFile = new File(ElMedievo.get().getDataFolder() + "/recipes", "furnace.xml");

    if (!furnaceXMLFile.exists()) {
      try {
        FileUtils.copyInputStreamToFile(
            Objects.requireNonNull(ElMedievo.get().getResource("furnace.xml")), furnaceXMLFile);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }

    try {
      furnaceXML = new SAXBuilder().build(furnaceXMLFile);
    } catch (JDOMException | IOException exception) {
      exception.printStackTrace();
    }

    loadRecipes();
  }

  public void reload() {
    recipes.clear();

    Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();

    while (iterator.hasNext()) {
      Recipe recipe = iterator.next();

      if (recipe instanceof FurnaceRecipe) {
        iterator.remove();
      }
    }

    Bukkit.resetRecipes();

    load();
    registerRecipes();
  }

  /** Adds all of the configured furnace recipes from furnace.xml to a list */
  public void loadRecipes() {
    Element furnaceXMLRootElement = furnaceXML.getRootElement();

    List<Element> furnaceRecipes = furnaceXMLRootElement.getChildren();
    furnaceRecipes.forEach(
        furnaceRecipe -> {
          String id = null;
          float experience = 0;
          int cookingTime = 0;
          Material source = null;
          Material result = null;
          int resultAmount = 0;

          try {
            id = parseRecipeId(furnaceRecipe);
            experience = parseRecipeExp(furnaceRecipe);
            cookingTime = parseRecipeCookingTime(furnaceRecipe);
            source = parseRecipeSource(furnaceRecipe);
            result = parseRecipeResult(furnaceRecipe);
            resultAmount = parseRecipeResultAmount(furnaceRecipe);
          } catch (RecipeXMLException exception) {
            exception.printStackTrace();
          }

          recipes.add(
              new CustomFurnaceRecipe(id, experience, cookingTime, source, result, resultAmount));
        });
  }

  /** Registers all of the custom recipes as Bukkit furnace recipes */
  public void registerRecipes() {
    recipes.forEach(
        recipe -> {
          NamespacedKey key = new NamespacedKey(ElMedievo.get(), recipe.getId());
          ItemStack result = new ItemStack(recipe.getResult(), recipe.getResultAmount());

          FurnaceRecipe customFurnaceRecipe =
              new FurnaceRecipe(
                  key, result, recipe.getSource(), recipe.getExperience(), recipe.getCookingTime());

          Bukkit.getServer().addRecipe(customFurnaceRecipe);
        });
  }

  /**
   * Parses the id="" attribute off a <recipe/> module from furnace.xml
   *
   * @param recipe <recipe/> module
   * @return Parsed id String
   * @throws RecipeXMLException All of the exceptions the attribute id="" could cause
   */
  private String parseRecipeId(Element recipe) throws RecipeXMLException {
    Attribute idAttribute = recipe.getAttribute("id");
    if (idAttribute == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.MISSING_ID_ATTRIBUTE);

    String idAttributeValue = idAttribute.getValue();
    if (String.valueOf(idAttribute) == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_ID);

    for (CustomFurnaceRecipe customFurnaceRecipe : recipes) {
      if (customFurnaceRecipe.getId().equals(idAttributeValue))
        throw new RecipeXMLException(RecipeXMLExceptionCause.DUPLICATED_ID);
    }

    return idAttributeValue;
  }

  /**
   * Parses the experience="" attribute off a <recipe/> module from furnace.xml
   *
   * @param recipe <recipe/> module
   * @return Parsed experience float
   * @throws RecipeXMLException All of the exceptions the attribute experience="" could cause
   */
  private float parseRecipeExp(Element recipe) throws RecipeXMLException {
    Attribute experienceAttribute = recipe.getAttribute("experience");
    if (experienceAttribute == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.MISSING_EXPERIENCE_ATTRIBUTE);

    try {
      return Float.parseFloat(experienceAttribute.getValue());
    } catch (NumberFormatException ignored) {
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_EXPERIENCE_LEVEL);
    }
  }

  /**
   * Parses the time="" attribute off a <recipe/> module from furnace.xml
   *
   * @param recipe <recipe/> module
   * @return Parsed time int
   * @throws RecipeXMLException All of the exceptions the attribute time="" could cause
   */
  private int parseRecipeCookingTime(Element recipe) throws RecipeXMLException {
    Attribute cookingTimeAttribute = recipe.getAttribute("time");
    if (cookingTimeAttribute == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.MISSING_COOKING_TIME_ATTRIBUTE);

    try {
      return Integer.parseInt(cookingTimeAttribute.getValue()) * 20;
    } catch (NumberFormatException ignored) {
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_COOKING_TIME);
    }
  }

  /**
   * Parses the <source/> sub-module of a <recipe/> module from furnace.xml
   *
   * @param recipe <recipe/> module
   * @return Material parsed from the <source/> sub-module content
   * @throws RecipeXMLException All of the exceptions the sub-module <source/> could cause
   */
  private Material parseRecipeSource(Element recipe) throws RecipeXMLException {
    Element sourceChildElement = recipe.getChild("source");
    if (sourceChildElement == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.MISSING_REQUIRED_CHILD_SOURCE);

    String sourceChildText = sourceChildElement.getText();
    if (sourceChildText == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.NO_SOURCE_EXCEPTION);

    try {
      String formattedSource = sourceChildText.replaceAll(" ", "_").toUpperCase();
      return Material.getMaterial(formattedSource);
    } catch (IllegalArgumentException ignored) {
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_SOURCE_MATERIAL);
    }
  }

  /**
   * Parses the <result/> sub-module of a <recipe/> module from furnace.xml
   *
   * @param recipe <recipe/> module
   * @return Material parsed from the <result/> sub-module's content
   * @throws RecipeXMLException All of the exceptions the sub-module <result/> could cause
   */
  private Material parseRecipeResult(Element recipe) throws RecipeXMLException {
    Element resultChildElement = recipe.getChild("result");
    if (resultChildElement == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.MISSING_REQUIRED_CHILD_RESULT);

    String resultChildText = resultChildElement.getText();
    if (resultChildText == null)
      throw new RecipeXMLException(RecipeXMLExceptionCause.NO_RESULT_EXCEPTION);

    try {
      String formattedResult = resultChildText.replaceAll(" ", "_").toUpperCase();
      return Material.getMaterial(formattedResult);
    } catch (IllegalArgumentException ignored) {
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_RESULT_MATERIAL);
    }
  }

  /**
   * Parses the amount="" attribute off a <result/> sub-module from furnace.xml
   *
   * @param recipe <recipe/> module containing the <result/> sub-module
   * @return Parsed result's amount int
   * @throws RecipeXMLException All of the exceptions the attribute amount="" could cause
   */
  private int parseRecipeResultAmount(Element recipe) throws RecipeXMLException {
    Attribute resultAmountAttribute = recipe.getChild("result").getAttribute("amount");
    if (resultAmountAttribute == null) return 1;

    try {
      return Integer.parseInt(resultAmountAttribute.getValue());
    } catch (NumberFormatException ignored) {
      throw new RecipeXMLException(RecipeXMLExceptionCause.INVALID_RESULT_AMOUNT);
    }
  }
}
