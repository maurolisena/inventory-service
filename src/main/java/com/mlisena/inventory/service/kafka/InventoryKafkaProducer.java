package com.mlisena.inventory.service.kafka;

import com.mlisena.inventory.configuration.kafka.KafkaProperties;
import com.mlisena.inventory.dto.payload.InventoryCreateRequest;
import com.mlisena.inventory.dto.payload.InventoryCreatedConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void confirmCreateInventoryEvent(InventoryCreatedConfirmation payload) {
        String topic = kafkaProperties.getTopics().getInventoryCreateRequest();
        String jsonPayload = KafkaUtils.serialize(payload);
        log.info("Producing inventory created event to kafka: {}", jsonPayload);

        try {
            kafkaTemplate.send(topic, payload.skuCode(), jsonPayload);
            log.info("Inventory created event sent successfully");
        } catch (Exception e) {
            log.error("Failed to send inventory created event to Kafka", e);
        }
    }
}
