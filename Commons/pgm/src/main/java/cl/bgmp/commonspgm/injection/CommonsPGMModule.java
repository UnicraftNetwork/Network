package cl.bgmp.commonspgm.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commonspgm.CommonsPGM;
import cl.bgmp.commonspgm.CommonsPGMConfig;
import cl.bgmp.commonspgm.Config;
import cl.bgmp.commonspgm.modules.manager.ModuleManager;
import cl.bgmp.commonspgm.modules.manager.ModuleManagerImpl;
import cl.bgmp.commonspgm.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CommonsPGMModule extends AbstractModule {
  private CommonsPGM commons;
  private CommonsPGMConfig config;
  private AllTranslations translations;
  private ModuleManagerImpl moduleManager;

  public CommonsPGMModule(
      CommonsPGM commons,
      CommonsPGMConfig config,
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
    this.bind(CommonsPGM.class).toInstance(this.commons);
    this.bind(Config.class).toInstance(this.config);
    this.bind(Translations.class).toInstance(this.translations);
    this.bind(ModuleManager.class).toInstance(this.moduleManager);
  }
}
