package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserData;

@RequiredArgsConstructor
public class MessageEnricher {
    private final ObjectMapper objectMapper;
    private final MessageValidator validator;

    public String enrich(String rawJson, UserData userData) throws Exception {
        ObjectNode node =(ObjectNode) objectMapper.readTree(rawJson);
        ObjectNode enrichment = objectMapper.createObjectNode();
        enrichment.put("firstName", userData.getFirstName());
        enrichment.put("lastName", userData.getLastName());
        node.set("enrichment", enrichment);
        return objectMapper.writeValueAsString(node);
    }
}

