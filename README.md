Concept
The user sends a DTO in the following format:
```java
public class Message {
    private String content;
    private EnrichmentType enrichmentType;
    public enum EnrichmentType {
        MSISDN;
    }
}
```java
The DTO contains the message content and an enrichment type enrichmentType.
MSISDN is enrichment based on a phone number. As a result of the enrichment, firstName and lastName are added to the enrichment field.
MSISDN enrichment conditions

The message must be in JSON format.

The JSON must have a field msisdn with a string value. Other fields are arbitrary.

For the given MSISDN, the corresponding information should be inserted.

If the enrichment field already exists in the message, it should be overwritten.

If one of the conditions is not met (message is not in JSON format, msisdn field is missing, or information is not found), the message is returned in the same form in which it was received.

Entry point
The entry point to the application should be the EnrichmentService class with an enrich method.

Implementation requirements

The system should work correctly in a multi-threaded environment. It is assumed that the enrich method may be called concurrently from different threads.

User information is stored in memory. It is assumed that it may change periodically in another thread.

Each processed message should additionally be saved in one of two data structures, depending on whether the message was successfully enriched or not. For example, a list of enriched and non-enriched messages.

At least one End-to-End test should be written to check that the system works correctly in a multi-threaded mode. As an option, you can use ExecutorService and CountDownLatch or Phaser to run multiple tasks simultaneously.
