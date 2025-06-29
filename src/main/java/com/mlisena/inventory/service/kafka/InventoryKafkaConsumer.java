package com.mlisena.inventory.service.kafka;

import com.mlisena.inventory.application.InventoryManager;
import com.mlisena.inventory.dto.CreateInventoryRequest;
import com.mlisena.inventory.dto.payload.CreateInventoryPayload;
import com.mlisena.inventory.mapper.InventoryKafkaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaConsumer {

    private final InventoryManager inventoryManager;

    @KafkaListener(
            topics = "${kafka.topics.inventory-create-request}",
            groupId = "${kafka.consumer.group-id}"
    )
    public void confirmInventoryCreatedEvent(String payload) {
        log.info("Consumed message from topic: {}", payload);
        CreateInventoryPayload createInventoryPayload = KafkaUtils.deserialize(payload, CreateInventoryPayload.class);
        CreateInventoryRequest request = InventoryKafkaMapper.toRequest(createInventoryPayload);
        inventoryManager.createInventory(request);
    }
}
