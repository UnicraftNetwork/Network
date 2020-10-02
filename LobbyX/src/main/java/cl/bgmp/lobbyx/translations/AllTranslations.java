package cl.bgmp.lobbyx.translations;

import cl.bgmp.butils.translations.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllTranslations extends Translations {

  public AllTranslations() {
    super(TranslationFiles.template, TranslationFiles.translations);
  }

  @Override
  public String getLocale(CommandSender commandSender) {
    if (!(commandSender instanceof Player)) return "es_cl";
    return ((Player) commandSender).getLocale();
  }

  @Override
  public void setLocale(CommandSender commandSender, String s) {}
}
