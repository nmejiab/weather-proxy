package io.github.nmejiab.weather_proxy.domain.services;

import io.github.nmejiab.weather_proxy.adapters.out.MockWeatherRepository;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherDTO;
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class WeatherServiceTest {

    private WeatherService weatherService;
    private IWeatherRepository weatherRepositoryService;
    private ILogWeatherRepository logWeatherRepository;

    @BeforeEach
    void setUp() {
        weatherRepositoryService = new MockWeatherRepository();
        logWeatherRepository = mock(ILogWeatherRepository.class);
        weatherService = new WeatherService();
        weatherService.setWeatherRepositoryService(weatherRepositoryService);
        weatherService.setLogWeatherRepository(logWeatherRepository);
    }

    @Test
    void testGetCurrentWeather_ReturnsDTO_WhenWeatherFound() {
        String city = "London";
        CurrentWeatherQueryConfig config = new CurrentWeatherQueryConfig();

        CurrentWeatherDTO result = weatherService.getCurrentWeather(city, config);

        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(15, result.getTemperature().getValue());
        assertEquals("°C", result.getTemperature().getUnit());
        assertEquals("Cloudy", result.getCondition());
        assertEquals(5, result.getWind().getSpeed());
        assertEquals("km/h", result.getWind().getUnit());
    }

    @Test
    void testGetCurrentWeather_ReturnsNull_WhenWeatherNotFound() {
        String city = "Unknown";
        CurrentWeatherQueryConfig config = new CurrentWeatherQueryConfig();

        CurrentWeatherDTO result = weatherService.getCurrentWeather(city, config);

        assertNull(result);
        verify(logWeatherRepository, never()).saveLog(anyString(), anyString(), anyString(), anyString());
    }
}