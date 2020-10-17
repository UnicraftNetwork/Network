package cl.bgmp.api.injection;

import cl.bgmp.api.APIBukkit;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class APIModule extends AbstractModule {
  private final APIBukkit apiBukkit;

  public APIModule(APIBukkit apiBukkit) {
    this.apiBukkit = apiBukkit;
  }

  public Injector createInjector() {
    return Guice.createInjector(this);
  }

  @Override
  protected void configure() {
    this.bind(APIBukkit.class).toInstance(this.apiBukkit);
  }
}
