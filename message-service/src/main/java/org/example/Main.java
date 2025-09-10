package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.Message;
import org.example.dto.UserData;
import org.example.repository.UserRepository;
import org.example.service.EnrichmentService;
import org.example.service.MessageEnricher;
import org.example.repository.MessageRepository;
import org.example.service.MessageValidator;
import org.example.service.MsisdnEnrichmentExtractor;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageValidator validator = new MessageValidator(objectMapper);
        MessageRepository store = new MessageRepository();
        MessageEnricher enricher = new MessageEnricher(objectMapper, validator);
        UserRepository userRepository = new UserRepository();
        MsisdnEnrichmentExtractor msisdnEnrichmentExtractor = new MsisdnEnrichmentExtractor(objectMapper);
        EnrichmentService service = new EnrichmentService(validator, store,enricher, objectMapper, userRepository,msisdnEnrichmentExtractor);
        userRepository.addUserData("88005553535", new UserData("John", "Doe"));

        Message message = new Message();
        message.setContent("{\"action\": \"button_click\", \"page\": \"book_card\", \"msisdn\": \"88005553535\"}");
        service.enrich(message);
    }
}