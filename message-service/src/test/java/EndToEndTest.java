import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.Message;
import org.example.dto.UserData;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.service.EnrichmentService;
import org.example.service.MessageEnricher;
import org.example.service.MessageValidator;
import org.example.service.MsisdnEnrichmentExtractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class EndToEndTest {
    private UserRepository userRepository;
    private MessageValidator messageValidator;
    private MessageEnricher enricher;
    private EnrichmentService enrichmentService;
    private MessageRepository messageRepository;
    private MsisdnEnrichmentExtractor msisdnEnrichmentExtractor;
    @BeforeEach
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        userRepository = new UserRepository();
        messageValidator = new MessageValidator(objectMapper);
        enricher = new MessageEnricher(objectMapper, messageValidator);
        messageRepository = new MessageRepository();
        msisdnEnrichmentExtractor = new MsisdnEnrichmentExtractor(objectMapper);
        enrichmentService = new EnrichmentService(messageValidator, messageRepository, enricher, objectMapper, userRepository, msisdnEnrichmentExtractor);

        userRepository.addUserData("88005553535", new UserData("John", "John"));
        userRepository.addUserData("88005553536", new UserData("Luke", "Luke"));
        userRepository.addUserData("88005553537", new UserData("Ed", "Ed"));
        }

    @ParameterizedTest
    @MethodSource("generateTestData")
    public void testConcurrentEnrichment(String messageContent, int threadCount, int taskCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            executorService.submit(() -> {
                try {
                    Message message = new Message();
                    message.setContent(messageContent);
                    String result = enrichmentService.enrich(message);

                    if (messageContent.equals("{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\"}")) {
                        Assertions.assertEquals("{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\",\"enrichment\":{\"firstName\":\"Vasya\",\"lastName\":\"Ivanov\"}}", result);
                    } else if (messageContent.equals("{\"msisdn\":\"88005553536\"}")) {
                        Assertions.assertEquals("{\"msisdn\":\"88005553536\",\"enrichment\":{\"firstName\":\"Lucya\",\"lastName\":\"Chebotina\"}}", result);
                    } else if (messageContent.equals("{\"msisdn\":\"88005553537\"}")) {
                        Assertions.assertEquals("{\"msisdn\":\"88005553537\",\"enrichment\":{\"firstName\":\"Egor\",\"lastName\":\"Kreed\"}}", result);
                    } else if (messageContent.equals("{\"msisdn\":\"88005553538\"}")) {
                        Assertions.assertEquals("{\"msisdn\":\"88005553538\",\"enrichment\":{\"firstName\":\"Guf\",\"lastName\":\"Dead\"}}", result);
                    }
                } catch (Exception e) {
                    Assertions.fail("Error during execution: " + e.getMessage());
                }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
    }

    private static Stream<Arguments> generateTestData() {
        return Stream.of(
                Arguments.of("{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\"}", 10, 10),
                Arguments.of("{\"msisdn\":\"88005553536\"}", 5, 5),
                Arguments.of("{\"msisdn\":\"88005553537\"}", 15, 15),
                Arguments.of("{\"msisdn\":\"88005553538\"}", 20, 20)
        );
    }

}


