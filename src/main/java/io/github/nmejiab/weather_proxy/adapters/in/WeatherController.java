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
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;


@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final Map<String, IWeatherRepository> weatherRepositories;
    private final ObjectMapper objectMapper;
    private final ILogWeatherRepository logWeatherRepository;

    /**
     * Constructs a new {@code WeatherController} with the required dependencies.
     *
     * @param weatherService         the service responsible for weather-related business logic
     * @param weatherRepositories    a map of weather repository implementations, keyed by identifier
     * @param objectMapper           the Jackson {@code ObjectMapper} for JSON serialization/deserialization
     * @param logWeatherRepository   the repository used for logging weather-related operations
     */
    public WeatherController(
        WeatherService weatherService,
        Map<String, IWeatherRepository> weatherRepositories,
        ObjectMapper objectMapper,
        ILogWeatherRepository logWeatherRepository
    ) {
        this.weatherService = weatherService;
        this.weatherRepositories = weatherRepositories;
        this.objectMapper = objectMapper;
        this.logWeatherRepository = logWeatherRepository;
    }

    /**
     * Handles HTTP GET requests to retrieve the current weather for a specified city.
     *
     * @param city   the name of the city for which to retrieve weather information (extracted from the path variable)
     * @param config a JSON string representing the configuration for the weather query (provided as a request parameter)
     * @param source the identifier of the weather data source to use (provided as a request parameter)
     * @return a {@link ResponseEntity} containing the {@link CurrentWeatherDTO} if successful,
     *         a bad request response if the config or source is invalid,
     *         or a not found response if the city is not found
     */
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
        if (currentWeather == null) {
            return ResponseEntity.notFound().build(); // Ciudad no encontrada
        }
        logWeatherRepository.saveLog(
            city, "success", source, null
        );
        return ResponseEntity.ok(currentWeather);
    }
}
