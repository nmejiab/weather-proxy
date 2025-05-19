package io.github.nmejiab.weather_proxy.domain.models;

import lombok.Data;

@Data
public class CurrenWeatherQueryConfig {
    private String apiKey;
    private String baseUrl;
}
