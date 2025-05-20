package io.github.nmejiab.weather_proxy.adapters.out;

import org.springframework.stereotype.Repository;

import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;

/**
 * Mock implementation of the {@link IWeatherRepository} interface for testing purposes.
 * <p>
 * This repository returns a fixed {@link CurrentWeather} object for any city except "Unknown".
 * If the city is "Unknown", it returns {@code null}.
 * </p>
 *
 * <p>
 * This class is annotated with {@code @Repository("mock")} to indicate it is a mock bean.
 * </p>
 */
@Repository("mock")
public class MockWeatherRepository implements IWeatherRepository {
    /**
     * Retrieves the current weather information for a specified city.
     * <p>
     * This is a mock implementation intended for testing purposes.
     * If the provided city is "Unknown", this method returns {@code null}.
     * Otherwise, it returns a {@link CurrentWeather} object with mock data.
     * </p>
     *
     * @param city   the name of the city for which to retrieve weather data
     * @param config the configuration for the weather query
     * @return a {@link CurrentWeather} object containing mock weather data,
     *         or {@code null} if the city is "Unknown"
     */
    @Override
    public CurrentWeather getWeatherByCity(String city, CurrentWeatherQueryConfig config) {
        if(city.equals("Unknown")) {
            return null;
        }
        // Mock implementation for testing purposes
        return new CurrentWeather(
            15,
            "Cloudy",
            5
        );
    }
    
}
