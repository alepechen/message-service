package org.example.dto;

import lombok.Data;

@Data
public class Message {
    private String content;
    private EnrichmentType enrichmentType;
    private String msisdn;

    public enum EnrichmentType {
        MSISDN {
        };
    }
}
