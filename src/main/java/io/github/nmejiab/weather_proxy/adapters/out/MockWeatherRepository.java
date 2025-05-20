package io.github.nmejiab.weather_proxy.adapters.out;

import org.springframework.stereotype.Repository;

import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;

@Repository("mock")
public class MockWeatherRepository implements IWeatherRepository {
    @Override
    public CurrentWeather getWeatherByCity(String city, CurrentWeatherQueryConfig config) {
        // Mock implementation for testing purposes
        return new CurrentWeather(
            15,
            "Cloudy",
            5
        );
    }
    
}
