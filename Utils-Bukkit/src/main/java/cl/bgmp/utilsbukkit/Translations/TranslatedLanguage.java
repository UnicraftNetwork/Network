package cl.bgmp.utilsbukkit.Translations;

import java.io.IOException;
import java.util.Properties;

public class TranslatedLanguage {
  private String locale;
  private Properties languagePropertiesFile = new Properties();

  public TranslatedLanguage(String locale) {
    this.locale = locale;
    loadFromResources(languagePropertiesFile, this.locale);
  }

  public String getLocale() {
    return locale;
  }

  public String get(final String key) {
    return languagePropertiesFile.getProperty(key);
  }

  private void loadFromResources(final Properties properties, final String locale) {
    try {
      properties.load(
          this.getClass().getClassLoader().getResourceAsStream("i18n/" + locale + ".properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
