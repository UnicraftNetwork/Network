package cl.bgmp.elmedievo.reciper.Furnace;

import org.bukkit.Material;

public class CustomFurnaceRecipe {
  private String id;
  private float experience;
  private int cookingTime;
  private Material source;
  private Material result;
  private int resultAmount;

  /**
   * CustomFurnaceRecipe constructor. Used to model and manage all of the configured recipes
   *
   * @param id An identifier required by Bukkit's FurnaceRecipe NamespacedKey
   * @param experience Experience level obtained after having cooked
   * @param cookingTime Cooking time
   * @param source Source material
   * @param result Resulting material
   * @param resultAmount Amount of resulting material obtained per consumed source
   */
  public CustomFurnaceRecipe(
      String id,
      float experience,
      int cookingTime,
      Material source,
      Material result,
      int resultAmount) {
    this.id = id;
    this.experience = experience;
    this.cookingTime = cookingTime;
    this.source = source;
    this.result = result;
    this.resultAmount = resultAmount;
  }

  public String getId() {
    return id;
  }

  public float getExperience() {
    return experience;
  }

  public int getCookingTime() {
    return cookingTime;
  }

  public Material getSource() {
    return source;
  }

  public Material getResult() {
    return result;
  }

  public int getResultAmount() {
    return resultAmount;
  }
}
