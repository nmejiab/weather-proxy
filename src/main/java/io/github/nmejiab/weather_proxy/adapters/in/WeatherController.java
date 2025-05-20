package io.github.nmejiab.weather_proxy.adapters.in;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.dtos.CurrentWeatherDTO;
import io.github.nmejiab.weather_proxy.domain.services.WeatherService;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;


@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final Map<String, IWeatherRepository> weatherRepositories;
    private final ObjectMapper objectMapper;

    public WeatherController(
        WeatherService weatherService, Map<String,
        IWeatherRepository> weatherRepositories,
        ObjectMapper objectMapper
    ) {
        this.weatherService = weatherService;
        this.weatherRepositories = weatherRepositories;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{city}")
    public ResponseEntity<CurrentWeatherDTO> getCurrentWeather(
        @PathVariable String city,
        @RequestParam String config,
        @RequestParam String source
    ) {
        CurrentWeatherQueryConfig currentWeatherQueryConfig;
        try {
            currentWeatherQueryConfig = objectMapper.readValue(
                config, CurrentWeatherQueryConfig.class
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Config no válida
        }

        IWeatherRepository selectedRepository = weatherRepositories.get(source);
        if (selectedRepository == null) {
            return ResponseEntity.badRequest().body(null); // Source no válido
        }

        weatherService.setWeatherRepositoryService(selectedRepository);
        CurrentWeatherDTO currentWeather = weatherService.getCurrentWeather(
            city, currentWeatherQueryConfig
        );
        return ResponseEntity.ok(currentWeather);
    }
}
