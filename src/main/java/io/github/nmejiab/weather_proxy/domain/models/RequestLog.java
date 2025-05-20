package io.github.nmejiab.weather_proxy.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "request_log")
@Data
public class RequestLog {
    @Id
    private String id;

    @Column(nullable = false, length = 32)
    private String city;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 7)
    private String status;

    @Column(nullable = false, length = 16)
    private String source;

    @Column(columnDefinition = "LONGTEXT")
    private String error;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        this.timestamp = LocalDateTime.now();
    }
}
