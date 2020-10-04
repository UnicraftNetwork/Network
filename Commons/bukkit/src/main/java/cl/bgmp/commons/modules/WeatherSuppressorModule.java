package cl.bgmp.commons.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherSuppressorModule extends Module {

  public WeatherSuppressorModule() {
    super(ModuleId.WEATHER);
  }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    final boolean toRain = event.toWeatherState();
    if (toRain) event.setCancelled(true);
  }

  @Override
  public boolean isEnabled() {
    return this.config.isWeatherDisabled();
  }
}
