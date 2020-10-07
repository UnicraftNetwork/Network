package cl.bgmp.commonspgm.translations;

import cl.bgmp.butils.files.PropertiesUtils;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Properties;

public interface TranslationFiles {
  Class<TranslationFiles> clazz = TranslationFiles.class;
  String ROOT = "i18n/";
  String TEMPLATE = ROOT + "templates/strings.properties";
  String TRANSLATIONS = ROOT + "translations/";

  Properties template = PropertiesUtils.newPropertiesResource(clazz, TEMPLATE);
  Map<String, Properties> translations =
      ImmutableMap.<String, Properties>builder()
          .put(
              "es_cl",
              PropertiesUtils.newPropertiesResource(clazz, TRANSLATIONS + "es_cl.properties"))
          .build();
}
