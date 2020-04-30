package cl.bgmp.utilsbukkit.Translations;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Translations {
  public static final Properties templatePropertiesFile = loadTemplatePropertiesFromResources();
  private static final ImmutableSet<TranslatedLanguage> translatedLanguages =
      ImmutableSet.copyOf(
          new HashSet<TranslatedLanguage>() {
            {
              add(new TranslatedLanguage("en_gb"));
              add(new TranslatedLanguage("es_cl"));
              add(new TranslatedLanguage("lol_us"));
              add(new TranslatedLanguage("es_mx"));
            }
          });

  public static String get(
      @NotNull String key, @NotNull CommandSender sender, @Nullable String... args) {
    if (sender instanceof ConsoleCommandSender)
      return applyArgsToTranslation(templatePropertiesFile.getProperty(key), args);

    final String locale = ((Player) sender).getLocale();
    final TranslatedLanguage translatedLanguage = getTranslatedLanguageByLocale(locale);

    if (translatedLanguage == null)
      return applyArgsToTranslation(templatePropertiesFile.getProperty(key), args);
    else return applyArgsToTranslation(translatedLanguage.get(key), args);
  }

  public static String applyArgsToTranslation(String translation, String... args) {
    if (args == null) return translation;

    int count = 0;
    for (String arg : args) {
      translation = translation.replace("{" + count + "}", arg);
      count++;
    }

    return translation;
  }

  @Nullable
  private static TranslatedLanguage getTranslatedLanguageByLocale(final String locale) {
    return translatedLanguages.stream()
        .filter(translatedLanguage -> translatedLanguage.getLocale().equals(locale))
        .findFirst()
        .orElse(null);
  }

  private static Properties loadTemplatePropertiesFromResources() {
    final Properties properties = new Properties();
    try {
      properties.load(
          Translations.class
              .getClassLoader()
              .getResourceAsStream("i18n/template/strings.properties"));
      return properties;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }
}
