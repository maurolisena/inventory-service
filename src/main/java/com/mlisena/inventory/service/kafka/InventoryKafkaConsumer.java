package com.mlisena.inventory.service.kafka;

import com.mlisena.inventory.dto.mapper.InventoryMapper;
import com.mlisena.inventory.dto.payload.InventoryCreateRequest;
import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "#{__listener.kafkaProperties.topics.inventoryCreateRequest}",
            groupId = "${kafka.consumer.group-id}"
    )
    public void confirmInventoryCreatedEvent(String payload) {
        log.info("Consumed message from topic: {}", payload);
        InventoryCreateRequest inventoryCreateRequest = KafkaUtils.deserialize(payload, InventoryCreateRequest.class);
        CreateInventoryRequest request = InventoryMapper.toRequest(inventoryCreateRequest);
        inventoryService.createInventory(request);
    }
}
