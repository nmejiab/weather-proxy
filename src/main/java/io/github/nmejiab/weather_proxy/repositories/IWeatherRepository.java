package io.github.nmejiab.weather_proxy.repositories;

import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;

public interface IWeatherRepository {
    CurrentWeather getWeatherByCity(String cityName, CurrentWeatherQueryConfig config);
}
