package io.github.nmejiab.weather_proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.nmejiab.weather_proxy.domain.services.WeatherService;

@Configuration
public class AppConfig {
    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
