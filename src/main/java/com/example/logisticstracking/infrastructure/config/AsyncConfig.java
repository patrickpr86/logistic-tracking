package com.example.logisticstracking.infrastructure.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

/**
 * Configuration for ThreadPoolTaskExecutor to handle asynchronous processing.
 * <p>
 * This executor is designed to manage a high volume of events and data retrieval,
 * ensuring scalability and stability. It dynamically adjusts the number of available
 * threads based on demand.
 * </p>
 *
 * @author Patrick Pascoal Ribeiro
 */
@Configuration
public class AsyncConfig {

    /**
     * Creates an asynchronous executor configured for concurrent task processing.
     * <p>
     * Configuration details:
     * <ul>
     *   <li><b>Core Pool Size:</b> ASYNC_CORE_POOL_SIZE - Minimum number of always-available threads.</li>
     *   <li><b>Max Pool Size:</b> ASYNC_MAX_POOL_SIZE - Maximum number of threads during peak loads.</li>
     *   <li><b>Queue Capacity:</b> ASYNC_QUEUE_CAPACITY - Number of pending tasks before rejecting new ones.</li>
     *   <li><b>Keep Alive Time:</b> ASYNC_KEEP_ALIVE_SECONDS - Time before idle threads are released.</li>
     *   <li><b>Allow Core Thread Timeout:</b> true - Allows core threads to be released when idle.</li>
     *   <li><b>Thread Name Prefix:</b> ASYNC_EXECUTOR_THREAD_PREFIX - Helps identify threads for monitoring.</li>
     * </ul>
     * </p>
     *
     * @return A configured Executor for asynchronous task processing.
     * @author Patrick Pascoal Ribeiro
     */
    @Bean
    public Executor kafkaEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ASYNC_CORE_POOL_SIZE);
        executor.setMaxPoolSize(ASYNC_MAX_POOL_SIZE);
        executor.setQueueCapacity(ASYNC_QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(ASYNC_KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix(ASYNC_EXECUTOR_THREAD_PREFIX);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }
}
