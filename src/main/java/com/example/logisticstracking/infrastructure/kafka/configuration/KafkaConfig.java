package com.example.logisticstracking.infrastructure.kafka.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public static final String TRACKING_TOPIC = "tracking-events";

    @Bean
    public NewTopic createTrackingTopic() {
        return new NewTopic(TRACKING_TOPIC, 3, (short) 1);
    }
}
