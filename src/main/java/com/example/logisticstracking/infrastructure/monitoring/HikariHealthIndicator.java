package com.example.logisticstracking.infrastructure.monitoring;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

/**
 * Custom Health Indicator for monitoring HikariCP connection pool.
 * <p>
 * This class exposes metrics such as active connections, idle connections,
 * total connections, and waiting threads in the connection pool.
 * </p>
 *
 * @author Patrick Pascoal Ribeiro
 */
@Component
public class HikariHealthIndicator implements HealthIndicator {

    private final HikariDataSource hikariDataSource;

    public HikariHealthIndicator(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail(ACTIVE_CONNECTIONS, hikariDataSource.getHikariPoolMXBean().getActiveConnections())
                .withDetail(IDLE_CONNECTIONS, hikariDataSource.getHikariPoolMXBean().getIdleConnections())
                .withDetail(TOTAL_CONNECTIONS, hikariDataSource.getHikariPoolMXBean().getTotalConnections())
                .withDetail(WAITING_THREADS, hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection())
                .build();
    }
}

