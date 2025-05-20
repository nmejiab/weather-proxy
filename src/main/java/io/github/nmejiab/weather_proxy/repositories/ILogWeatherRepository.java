package io.github.nmejiab.weather_proxy.repositories;

public interface ILogWeatherRepository {
    void saveLog(String city, String status, String source, String error);
}
