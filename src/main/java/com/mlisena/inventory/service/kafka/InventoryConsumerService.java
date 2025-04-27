package com.mlisena.inventory.service.kafka;

import com.mlisena.inventory.dto.mapper.InventoryMapper;
import com.mlisena.inventory.dto.payload.CreateInventoryEvent;
import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumerService {

    private final InventoryService inventoryService;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topics.inventory-created}")
    private String inventoryCreatedTopic;

    @KafkaListener(topics = "${kafka.topics.inventory-created}", groupId = "${kafka.consumer.group-id}")
    public void consumeInventoryCreatedEvent(@Payload CreateInventoryEvent payload) {
        log.info("Consumed message from topic: {}", payload);
        CreateInventoryRequest request = InventoryMapper.toRequest(payload);
        inventoryService.createInventory(request);
    }
}
