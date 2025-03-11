package com.example.logisticstracking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class LogisticsTrackingApplication implements CommandLineRunner {

	@Value("${kafka.tracking.topic-name:NOT_FOUND}")
	private String kafkaTopic;

	public static void main(String[] args) {
		SpringApplication.run(LogisticsTrackingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Kafka Topic: " + kafkaTopic);
	}
}
