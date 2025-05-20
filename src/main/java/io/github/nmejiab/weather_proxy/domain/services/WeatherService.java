package io.github.nmejiab.weather_proxy.domain.services;


import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherDTO;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherTemperatureDTO;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherWindDTO;
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class WeatherService {
    private IWeatherRepository weatherRepositoryService;
    private ILogWeatherRepository logWeatherRepository;
    
    public CurrentWeatherDTO getCurrentWeather(String city, CurrentWeatherQueryConfig config) {
        CurrentWeather currentWeather = null;
        try {
            currentWeather = weatherRepositoryService.getWeatherByCity(city, config);
        }
        catch (Exception e) {
            logWeatherRepository.saveLog(city, "fail", "WEATHER_API", e.getMessage());
            return null;
        }
        if (currentWeather == null) {
            return null;
        }
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
