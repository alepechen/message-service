package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageValidator {

    private final ObjectMapper objectMapper;

    public boolean isValid(String json) {
        try {
            ObjectNode node = (ObjectNode) objectMapper.readTree(json);
            return node.has("msisdn") && node.get("msisdn").isTextual();
        } catch (Exception e) {
            return false;
        }
    }
}

