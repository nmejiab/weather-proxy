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
import io.github.nmejiab.weather_proxy.repositories.IWeatherRepository;
import lombok.Data;

@Repository("openweather")
@Primary
@Data
public class WeatherRepository implements IWeatherRepository{
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${weatherstack.default-base-url}")
    private String apiUrl;

    @Value("${weatherstack.default-access-key}")
    private String apiKey;

    public WeatherRepository(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

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
        String response = restTemplate.getForObject(request, String.class);
        JsonNode jsonResponse;
        try{
            jsonResponse = objectMapper.readTree(response);
        }catch(Exception e){
            // Implement logging, storing and return null
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
            // Implement logging, storing and return null
            return null;
        }

        int windSpeed = 0;
        try{
            windSpeed = jsonResponse.get("current").get("wind_speed").asInt();
        }catch(Exception e){
            // Implement logging, storing and return null
            return null;
        }
        
        return CurrentWeather.builder()
            .temperature(temperature)
            .weatherCondition(weatherCondition)
            .windSpeed(windSpeed)
            .build();
    }

}
