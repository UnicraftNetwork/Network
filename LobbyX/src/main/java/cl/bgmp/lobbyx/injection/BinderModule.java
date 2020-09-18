package cl.bgmp.lobbyx.injection;

import cl.bgmp.butils.translations.Translations;
import cl.bgmp.lobbyx.Config;
import cl.bgmp.lobbyx.LobbyX;
import cl.bgmp.lobbyx.LobbyXConfig;
import cl.bgmp.lobbyx.translations.AllTranslations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BinderModule extends AbstractModule {
  private final LobbyX plugin;
  private final LobbyXConfig config;
  private final AllTranslations translations;

  public BinderModule(LobbyX plugin, LobbyXConfig config, AllTranslations translations) {
    this.plugin = plugin;
    this.config = config;
    this.translations = translations;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(LobbyX.class).toInstance(this.plugin);
    this.bind(Config.class).toInstance(this.config);
    this.bind(Translations.class).toInstance(this.translations);
  }
}
