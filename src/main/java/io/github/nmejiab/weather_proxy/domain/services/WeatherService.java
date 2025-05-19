package io.github.nmejiab.weather_proxy.domain.services;

import org.springframework.beans.factory.annotation.Value;

import io.github.nmejiab.weather_proxy.domain.models.CurrenWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherDTO;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherTemperatureDTO;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherWindDTO;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;

//import org.springframework.stereotype.Service;

// @Service
public class WeatherService {
    @Value("${weatherstack.default-access-key}")
    private String a;
    private IWeatherRepository weatherRepositoryService;
    public CurrentWeatherDTO getCurrentWeather(String city, CurrenWeatherQueryConfig config) {
        CurrentWeather currentWeather = weatherRepositoryService.getWeatherByCity(city, config);
        return convertToDTO(currentWeather, city);
    }

    private CurrentWeatherDTO convertToDTO(
        CurrentWeather currentWeather,
        String city
    ) {
        CurrentWeatherTemperatureDTO temperature = new CurrentWeatherTemperatureDTO(
            currentWeather.getTemperature(),
            "°C"
        );
        CurrentWeatherWindDTO wind = new CurrentWeatherWindDTO(
            currentWeather.getWindSpeed(),
            "km/h"
        );
        
        return CurrentWeatherDTO.builder()
            .city(city)
            .temperature(temperature)
            .condition(currentWeather.getWeatherCondition())
            .wind(wind)
            .build();
    }
}
