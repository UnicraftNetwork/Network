package cl.bgmp.commons.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commons.Commons;
import cl.bgmp.commons.CommonsConfig;
import cl.bgmp.commons.Config;
import cl.bgmp.commons.modules.manager.ModuleManager;
import cl.bgmp.commons.modules.manager.ModuleManagerImpl;
import cl.bgmp.commons.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CommonsModule extends AbstractModule {
  private Commons commons;
  private CommonsConfig config;
  private AllTranslations translations;
  private ModuleManagerImpl moduleManager;

  public CommonsModule(
      Commons commons,
      CommonsConfig config,
      AllTranslations translations,
      ModuleManagerImpl moduleManager) {
    this.commons = commons;
    this.config = config;
    this.translations = translations;
    this.moduleManager = moduleManager;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(Commons.class).toInstance(this.commons);
    this.bind(Config.class).toInstance(this.config);
    this.bind(Translations.class).toInstance(this.translations);
    this.bind(ModuleManager.class).toInstance(this.moduleManager);
  }
}
