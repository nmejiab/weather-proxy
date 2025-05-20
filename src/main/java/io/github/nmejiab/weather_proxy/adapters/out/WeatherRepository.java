package io.github.nmejiab.weather_proxy.adapters.out;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nmejiab.weather_proxy.domain.models.CurrentWeatherQueryConfig;
import io.github.nmejiab.weather_proxy.domain.models.CurrentWeather;
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;
import lombok.Data;

/**
 * WeatherRepository is a Spring-managed repository implementation for fetching current weather data
 * from the Weatherstack API. It uses RestTemplate for HTTP requests and ObjectMapper for JSON parsing.
 * The repository supports dynamic configuration via {@link CurrentWeatherQueryConfig} and logs all
 * request attempts and failures using {@link ILogWeatherRepository}.
 *
 * <p>
 * The repository is marked as the primary bean for the "openweather" qualifier.
 * </p>
 *
 * <p>
 * Configuration properties:
 * <ul>
 *   <li><b>weatherstack.default-base-url</b>: The default base URL for the Weatherstack API.</li>
 *   <li><b>weatherstack.default-access-key</b>: The default access key for the Weatherstack API.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Main responsibilities:
 * <ul>
 *   <li>Builds and sends HTTP requests to the Weatherstack API to retrieve current weather data by city name.</li>
 *   <li>Parses the JSON response to extract temperature, weather conditions, and wind speed.</li>
 *   <li>Handles errors gracefully and logs failures for monitoring and debugging purposes.</li>
 *   <li>Allows overriding of API URL and key via {@link CurrentWeatherQueryConfig}.</li>
 * </ul>
 * </p>
 *
 * @author [Your Name]
 * @see IWeatherRepository
 * @see CurrentWeather
 * @see CurrentWeatherQueryConfig
 * @see ILogWeatherRepository
 */
@Repository("openweather")
@Primary
@Data
public class WeatherRepository implements IWeatherRepository{
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ILogWeatherRepository logWeatherRepository;
    
    @Value("${weatherstack.default-base-url}")
    private String apiUrl;

    @Value("${weatherstack.default-access-key}")
    private String apiKey;

    public WeatherRepository(
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        ILogWeatherRepository logWeatherRepository
    ) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.logWeatherRepository = logWeatherRepository;
    }

    /**
     * Retrieves the current weather information for a given city.
     * <p>
     * This method queries a weather API using the provided city name and an optional configuration.
     * If the configuration is provided, it overrides the default API URL and API key.
     * The method constructs the request, sends it using a RestTemplate, and parses the JSON response.
     * If the request or parsing fails, it logs the error and returns {@code null}.
     * </p>
     *
     * @param cityName the name of the city for which to retrieve weather information
     * @param config an optional configuration object containing API URL and API key; if {@code null}, default values are used
     * @return a {@link CurrentWeather} object containing temperature, weather condition, and wind speed,
     *         or {@code null} if the request fails or the response is invalid
     */
    @Override
    public CurrentWeather getWeatherByCity(String cityName, CurrentWeatherQueryConfig config){
        // Set the config if is available
        // If the config is null, use the default values
        if(config != null){
            if(config.getBaseUrl() != null){
                apiUrl = config.getBaseUrl();
            }
            if(config.getApiKey() != null){
                apiKey = config.getApiKey();
            }
        }
        String request = UriComponentsBuilder.fromUriString(apiUrl)
            .queryParam("query", cityName)
            .queryParam("access_key", apiKey)
            .queryParam("units", "m")
            .build()
            .toUriString();
        
        String response;
        try {
            response = restTemplate.getForObject(request, String.class);
        } catch (Exception e) {
            logWeatherRepository.saveLog(cityName, "fail", "openweather", "HTTP request failed: " + e.getMessage());
            return null;
        }
        
        JsonNode jsonResponse;
        try{
            jsonResponse = objectMapper.readTree(response);
        }catch(Exception e){
            logWeatherRepository.saveLog(cityName, "fail", "openweather", e.getMessage());
            return null;
        }

        if(jsonResponse == null || (jsonResponse.has("success") && !jsonResponse.get("success").asBoolean())){
            logWeatherRepository.saveLog(cityName, "fail", "openweather", "Invalid response: " + response);
            return null;
        }

        JsonNode conditionsNode = jsonResponse.get("current").get("weather_descriptions");

        List<String> weatherConditions = new ArrayList<>();
        if(conditionsNode !=null && conditionsNode.isArray()){
            weatherConditions = StreamSupport.stream(conditionsNode.spliterator(), false)
                .map(arg0 -> arg0.asText())
                .collect(Collectors.toList());
        }
        String weatherCondition = weatherConditions.isEmpty() ? "Unknown" :
            String.join(", ", weatherConditions);

        int temperature = 0;
        try{
            temperature = jsonResponse.get("current").get("temperature").asInt();
        }catch(Exception e){
            logWeatherRepository.saveLog(cityName, "fail", "openweather", e.getMessage());
            return null;
        }

        int windSpeed = 0;
        try{
            windSpeed = jsonResponse.get("current").get("wind_speed").asInt();
        }catch(Exception e){
            logWeatherRepository.saveLog(cityName, "fail", "openweather", e.getMessage());
            return null;
        }
        
        return CurrentWeather.builder()
            .temperature(temperature)
            .weatherCondition(weatherCondition)
            .windSpeed(windSpeed)
            .build();
    }

}
