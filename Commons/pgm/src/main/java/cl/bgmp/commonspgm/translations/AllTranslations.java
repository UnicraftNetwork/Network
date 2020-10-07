package cl.bgmp.commonspgm.translations;

import cl.bgmp.butils.translations.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllTranslations extends Translations {

  public AllTranslations() {
    super(TranslationFiles.template, TranslationFiles.translations);
  }

  @Override
  public String getLocale(CommandSender sender) {
    if (!(sender instanceof Player)) return "es_cl";
    return ((Player) sender).getLocale().getDisplayLanguage().toLowerCase();
  }

  @Override
  public void setLocale(CommandSender sender, String locale) {}
}
