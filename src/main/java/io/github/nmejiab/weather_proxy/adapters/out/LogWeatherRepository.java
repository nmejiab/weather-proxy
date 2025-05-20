package io.github.nmejiab.weather_proxy.adapters.out;

import org.springframework.stereotype.Repository;

import io.github.nmejiab.weather_proxy.domain.models.RequestLog;
import io.github.nmejiab.weather_proxy.repositories.ILogWeatherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Repository implementation for logging weather request details into the database.
 * <p>
 * This class provides functionality to persist weather request logs, including information
 * about the city, request status, data source, and any error messages encountered during
 * the request process.
 * </p>
 * 
 * <p>
 * Usage of this repository allows tracking and auditing of weather data requests,
 * which can be useful for monitoring, debugging, and analytics purposes.
 * </p>
 *
 * <p>
 * This class is managed by Spring and should be injected where weather request logging is required.
 * </p>
 *
 * @see ILogWeatherRepository
 * @see RequestLog
 */
@Repository
public class LogWeatherRepository implements ILogWeatherRepository{
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persists a new weather request log entry with the specified details.
     *
     * @param city   the name of the city for which the weather was requested
     * @param status the status of the weather request (e.g., "SUCCESS", "FAILURE")
     * @param source the source from which the weather data was retrieved
     * @param error  the error message if the request failed, or {@code null} if successful
     */
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
