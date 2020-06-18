package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherModule extends Module {

  public WeatherModule() {
    super(ModuleId.WEATHER, Config.Weather.isDisabled());
  }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    final boolean toRain = event.toWeatherState();
    if (toRain) event.setCancelled(true);
  }

  @Override
  public void load() {
    if (enabled) Commons.get().registerEvents(this);
  }

  @Override
  public void unload() {
    setEnabled(Config.Weather.isDisabled());
    Commons.get().unregisterEvents(this);
  }
}
