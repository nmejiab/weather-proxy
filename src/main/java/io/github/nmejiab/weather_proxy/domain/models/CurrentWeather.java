package io.github.nmejiab.weather_proxy.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentWeather {
    private int temperature;
    private String weatherCondition;
    private int windSpeed;
}
