package com.mlisena.inventory.configuration.kafka;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
@Slf4j
public class KafkaProperties {

    private Topics topics;

    @Getter
    @Setter
    public static class Topics {
        private String inventoryCreateRequest;
        private String inventoryCreatedConfirmation;
    }

    @PostConstruct
    public void logTopics() {
        log.info("Kafka topic [inventory-created]: {}", topics.getInventoryCreateRequest());
        log.info("Kafka topic [inventory-create-confirmation]: {}", topics.getInventoryCreatedConfirmation());
    }
}
