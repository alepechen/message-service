package org.example.dto;

import lombok.Data;

@Data
public class EnrichedMessage {
    private String action;
    private String page;
    private String msisdn;
    private Enrichment enrichment;

    @Data
    public static class Enrichment {
        private String firstName;
        private String lastName;
    }
}
