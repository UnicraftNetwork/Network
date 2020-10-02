package cl.bgmp.commons.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commons.Commons;
import cl.bgmp.commons.CommonsConfig;
import cl.bgmp.commons.Config;
import cl.bgmp.commons.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BinderModule extends AbstractModule {
  private final Commons plugin;
  private final CommonsConfig config;
  private final AllTranslations translations;

  public BinderModule(Commons plugin, CommonsConfig config, AllTranslations translations) {
    this.plugin = plugin;
    this.config = config;
    this.translations = translations;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(Commons.class).toInstance(this.plugin);
    this.bind(Config.class).toInstance(this.config);
    this.bind(Translations.class).toInstance(this.translations);
  }
}
