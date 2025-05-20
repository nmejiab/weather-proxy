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

/**
 * Service class responsible for retrieving and processing weather information.
 * <p>
 * This service interacts with weather data repositories to fetch current weather
 * information for a specified city and converts the data into DTOs suitable for
 * external consumption.
 * </p>
 *
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link IWeatherRepository} for accessing weather data sources.</li>
 *   <li>{@link ILogWeatherRepository} for logging weather queries (not used in current implementation).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Main responsibilities:
 * <ul>
 *   <li>Fetch current weather data for a given city using configurable query parameters.</li>
 *   <li>Convert domain weather models to DTOs for API responses.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     CurrentWeatherDTO weather = weatherService.getCurrentWeather("London", config);
 * </pre>
 * </p>
 *
 * @author Your Name
 */
@Service
@NoArgsConstructor
@Data
public class WeatherService {
    private IWeatherRepository weatherRepositoryService;
    private ILogWeatherRepository logWeatherRepository;
    
    /**
     * Retrieves the current weather information for the specified city using the provided query configuration.
     *
     * @param city   the name of the city for which to retrieve the weather data
     * @param config the configuration options for querying the current weather
     * @return a {@link CurrentWeatherDTO} containing the weather data for the specified city,
     *         or {@code null} if no weather data is found
     */
    public CurrentWeatherDTO getCurrentWeather(String city, CurrentWeatherQueryConfig config) {
        CurrentWeather currentWeather = null;
        currentWeather = weatherRepositoryService.getWeatherByCity(city, config);
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
