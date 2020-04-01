package cl.bgmp.elmedievo.Translations;

import cl.bgmp.elmedievo.Translations.Languages.EN_GB;
import cl.bgmp.elmedievo.Translations.Languages.ES_CL;
import cl.bgmp.elmedievo.Translations.Languages.ES_ES;
import cl.bgmp.elmedievo.Translations.Languages.LOL_US;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Translator {

  public static String translate(String locale, ChatConstant toTranslate) {
    switch (locale) {
      case "en_gb":
        Optional<EN_GB> en_gbTranslation =
            Arrays.stream(EN_GB.values())
                .filter(en_gb -> en_gb.getChatConstant().equals(toTranslate))
                .findFirst();
        if (en_gbTranslation.isPresent()) return en_gbTranslation.get().getTranslation();
        break;
      case "es_es":
        Optional<ES_ES> esTranslation =
            Arrays.stream(ES_ES.values())
                .filter(es_es -> es_es.getChatConstant().equals(toTranslate))
                .findFirst();
        if (esTranslation.isPresent()) return esTranslation.get().getTranslation();
        break;
      case "es_cl":
        Optional<ES_CL> es_clTranslation =
            Arrays.stream(ES_CL.values())
                .filter(es_cl -> es_cl.getChatConstant().equals(toTranslate))
                .findFirst();
        if (es_clTranslation.isPresent()) return es_clTranslation.get().getTranslation();
        break;
      case "lol_us":
        Optional<LOL_US> lol_Translation =
            Arrays.stream(LOL_US.values())
                .filter(lol_us -> lol_us.getChatConstant().equals(toTranslate))
                .findFirst();
        if (lol_Translation.isPresent()) return lol_Translation.get().getTranslation();
        break;
    }
    return toTranslate.getString();
  }

  public static String translate(CommandSender sender, ChatConstant toTranslate) {
    if (!(sender instanceof Player)) return toTranslate.getString();
    else return translate(((Player) sender).getLocale(), toTranslate);
  }

  public static void notifyMissingTranslations(final Logger logger) {
    final Class chatConstant = ChatConstant.class;
    final Field[] constants = chatConstant.getDeclaredFields();
    final Set<Field> missingTranslations =
        Arrays.stream(constants)
            .filter(constant -> constant.isAnnotationPresent(MissingTranslation.class))
            .collect(Collectors.toSet());

    logger.info(missingTranslations.size() + " translations are missing!");
  }
}
