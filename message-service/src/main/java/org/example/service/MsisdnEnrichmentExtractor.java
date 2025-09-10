package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class MsisdnEnrichmentExtractor implements EnrichmentExtractor{
    private final ObjectMapper objectMapper;

    @Override
    public Optional<String> extractEnrichmentType(String rawJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(rawJson);
            JsonNode msisdnNode = jsonNode.get("msisdn");
            return Optional.ofNullable(msisdnNode)
                    .filter(msisdn -> !msisdn.isNull())  // Filter out the null or missing nodes
                    .map(JsonNode::asText);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
