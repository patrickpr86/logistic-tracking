package com.example.logisticstracking.infrastructure.kafka.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Kafka configuration class responsible for topic creation.
 */
@Configuration
@DependsOn("kafkaAdmin")
public class KafkaConfig {

    @Value("${kafka.tracking.topic-name}")
    private String trackingTopicName;

    @Value("${kafka.tracking.partitions}")
    private int trackingTopicPartitions;

    @Value("${kafka.tracking.replication-factor}")
    private int trackingTopicReplication;

    /**
     * Creates a Kafka topic for tracking events using externalized configuration.
     *
     * @return A configured NewTopic instance.
     */
    @Bean
    public NewTopic createTrackingTopic() {
        return new NewTopic(trackingTopicName, trackingTopicPartitions, (short) trackingTopicReplication);
    }
}
