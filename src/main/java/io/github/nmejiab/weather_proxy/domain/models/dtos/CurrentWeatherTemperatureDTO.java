package io.github.nmejiab.weather_proxy.domain.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentWeatherTemperatureDTO {
    private int value;
    private String unit;
}
