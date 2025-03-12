package com.example.logisticstracking.infrastructure.kafka.configuration;

import com.example.logisticstracking.infrastructure.kafka.dto.TrackingEventKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private static final String TRACKING_TOPIC = "tracking-events";
    private static final String DEAD_LETTER_TOPIC = "tracking-events-dlt";

    @Bean
    public NewTopic trackingTopic() {
        return new NewTopic(TRACKING_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic deadLetterTopic() {
        return new NewTopic(DEAD_LETTER_TOPIC, 3, (short) 1);
    }

    @Bean
    public ProducerFactory<String, TrackingEventKafkaDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, TrackingEventKafkaDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
