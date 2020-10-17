package cl.bgmp.bungee.translations;

import cl.bgmp.butils.translations.Translations;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AllTranslations extends Translations {

  public AllTranslations() {
    super(TranslationFiles.template, TranslationFiles.translations);
  }

  @Override
  public String getLocale(Object o) {
    if (o instanceof CommandSender) {
      CommandSender sender = (CommandSender) o;
      if (!(sender instanceof ProxiedPlayer)) return "es_cl";
      return ((ProxiedPlayer) sender).getLocale().getDisplayLanguage().toLowerCase();
    } else {
      return "es_cl";
    }
  }

  @Override
  public void setLocale(Object o, String locale) {}
}
