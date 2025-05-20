package io.github.nmejiab.weather_proxy.adapters.out;

import org.springframework.stereotype.Repository;

import io.github.nmejiab.weather_proxy.domain.models.RequestLog;
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class LogWeatherRepository implements ILogWeatherRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveLog(String city, String status, String source, String error) {
        RequestLog log = new RequestLog();
        log.setCity(city);
        log.setStatus(status);
        log.setSource(source);
        log.setError(error);
        entityManager.persist(log);
    }
}
