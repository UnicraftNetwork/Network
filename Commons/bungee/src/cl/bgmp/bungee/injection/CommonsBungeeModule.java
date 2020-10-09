package cl.bgmp.bungee.injection;

import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.MultiResolver;
import cl.bgmp.bungee.NetworkInfoProvider;
import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.bungee.translations.AllTranslations;
import cl.bgmp.butils.translations.Translations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CommonsBungeeModule extends AbstractModule {
  private final CommonsBungee commonsBungee;
  private final AllTranslations translations;
  private final NetworkInfoProvider networkInfoProvider;
  private final ChannelsManager channelsManager;
  private final MultiResolver multiResolver;

  public CommonsBungeeModule(
      CommonsBungee commonsBungee,
      AllTranslations translations,
      NetworkInfoProvider networkInfoProvider,
      ChannelsManager channelsManager,
      MultiResolver multiResolver) {
    this.commonsBungee = commonsBungee;
    this.translations = translations;
    this.networkInfoProvider = networkInfoProvider;
    this.channelsManager = channelsManager;
    this.multiResolver = multiResolver;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(CommonsBungee.class).toInstance(this.commonsBungee);
    this.bind(Translations.class).toInstance(this.translations);
    this.bind(NetworkInfoProvider.class).toInstance(this.networkInfoProvider);
    this.bind(ChannelsManager.class).toInstance(this.channelsManager);
    this.bind(MultiResolver.class).toInstance(this.multiResolver);
  }
}
