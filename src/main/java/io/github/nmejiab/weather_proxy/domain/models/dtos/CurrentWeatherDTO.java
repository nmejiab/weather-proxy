package io.github.nmejiab.weather_proxy.domain.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentWeatherDTO {
    private String city;
    private CurrentWeatherTemperatureDTO temperature;
    private String condition;
    private CurrentWeatherWindDTO wind;
}
