package com.mlisena.inventory.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error serializing object to JSON: {}", e.getMessage());
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    public static <T> T deserialize(String event, Class<T> targetClass) {
        try {
            return objectMapper.readValue(event, targetClass);
        } catch (Exception e) {
            log.error("Error deserializing JSON to object: {}", e.getMessage());
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }
}
