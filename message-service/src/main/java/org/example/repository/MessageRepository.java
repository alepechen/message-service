package org.example.repository;

import org.example.dto.EnrichedMessage;
import org.example.dto.Message;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageRepository {
    private final List<EnrichedMessage> enrichedMessages = new CopyOnWriteArrayList<>();
    private final List<Message> failedMessages = new CopyOnWriteArrayList<>();

    public void addEnriched(EnrichedMessage message) {
        enrichedMessages.add(message);
    }

    public void addFailed(Message message) {
        failedMessages.add(message);
    }
}
