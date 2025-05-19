package io.github.nmejiab.weather_proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WeatherProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherProxyApplication.class, args);
	}

}
