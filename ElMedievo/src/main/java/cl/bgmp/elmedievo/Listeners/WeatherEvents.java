package cl.bgmp.elmedievo.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherEvents implements Listener {

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    final boolean toRain = event.toWeatherState();
    if (toRain) event.setCancelled(true);
  }
}
