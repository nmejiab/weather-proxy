package io.github.nmejiab.weather_proxy.domain.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentWeatherWindDTO {
    private int speed;
    private String unit;
}
