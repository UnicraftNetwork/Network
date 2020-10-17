package cl.bgmp.bungee.injection;

import cl.bgmp.bungee.APIBungee;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class APIBungeeModule extends AbstractModule {
  private final APIBungee api;;

  public APIBungeeModule(APIBungee api) {
    this.api = api;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(APIBungee.class).toInstance(api);
  }
}
