package org.example.service;

import java.util.Optional;

public interface EnrichmentExtractor {
    Optional<String> extractEnrichmentType(String rawJson);
}
