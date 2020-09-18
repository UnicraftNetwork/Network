package cl.bgmp.commons.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.commons.Commons;
import cl.bgmp.commons.CommonsConfig;
import cl.bgmp.commons.Config;
import cl.bgmp.commons.modules.modulemanager.ModuleManager;
import cl.bgmp.commons.modules.modulemanager.ModuleManagerImpl;
import cl.bgmp.commons.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BinderModule extends AbstractModule {
  private final Commons plugin;
  private final CommonsConfig config;
  private final AllTranslations translations;
  private final ModuleManagerImpl moduleManager;

  public BinderModule(Commons plugin, CommonsConfig config, AllTranslations translations, ModuleManagerImpl moduleManager) {
    this.plugin = plugin;
    this.config = config;
    this.translations = translations;
    this.moduleManager = moduleManager;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(Commons.class).toInstance(this.plugin);
    this.bind(Config.class).toInstance(this.config);
    this.bind(Translations.class).toInstance(this.translations);
    this.bind(ModuleManager.class).toInstance(this.moduleManager);
  }
}
