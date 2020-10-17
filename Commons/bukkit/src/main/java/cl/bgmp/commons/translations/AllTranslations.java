package cl.bgmp.commons.translations;

import cl.bgmp.butils.translations.Translations;
import org.bukkit.entity.Player;

public class AllTranslations extends Translations {

  public AllTranslations() {
    super(TranslationFiles.template, TranslationFiles.translations);
  }

  @Override
  public String getLocale(Object sender) {
    if (!(sender instanceof Player)) return "es_cl";
    return ((Player) sender).getLocale();
  }

  @Override
  public void setLocale(Object sender, String locale) {}
}
