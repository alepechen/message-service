package org.example.service;

import org.example.dto.EnrichedMessage;
import org.example.dto.Message;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserData;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import java.util.Optional;

@RequiredArgsConstructor
public class EnrichmentService {
    private final MessageValidator validator;
    private final MessageRepository messageStore;
    private final MessageEnricher enricher;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final MsisdnEnrichmentExtractor msisdnEnrichmentExtractor;

    public String enrich(Message message) {
        String content = message.getContent();
        Optional<String> msisdnOpt= msisdnEnrichmentExtractor.extractEnrichmentType(content);
        String msisdn = msisdnOpt.orElse(null);
        Optional<UserData> userOpt = userRepository.getUserData(msisdn);
        if (!userOpt.isPresent()) {
            messageStore.addFailed(message);
            return message.toString();
        }

         if (validator.isValid(content)) {
                try {
                    UserData user = userOpt.get();
                    String enrichedJson = enricher.enrich(content, user);
                    EnrichedMessage enrichedMessage = objectMapper.readValue(enrichedJson, EnrichedMessage.class);
                   System.out.println(enrichedMessage);
                    messageStore.addEnriched(enrichedMessage);
                    return enrichedMessage.toString();
                } catch (Exception e) {
                     messageStore.addFailed(message);
                    return message.toString();
                }
           } else {
                messageStore.addFailed(message);
                return message.toString();
           }

    }
}
