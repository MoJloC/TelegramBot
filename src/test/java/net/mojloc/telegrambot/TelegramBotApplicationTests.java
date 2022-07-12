package net.mojloc.telegrambot;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Slf4j
class TelegramBotApplicationTests {
    @Autowired
    private ConfigurableApplicationContext context;
    private static RestTemplate restTemplate;

    @BeforeAll
    static void init() {
        log.info("Tests started");
        restTemplate = new RestTemplate();
    }

    @Test
    void testContextLoads() {
        Assertions.assertTrue(context.isActive());
        log.info("Application Context {} is active", context);
    }

    @Test
    void testWebConnection() {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/rest/v1/ConnectionTest",
                                                              "", String.class);
        log.info("************************* " + response.getStatusCodeValue() + " *************************");
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @AfterAll
    static void finish() {
        log.info("Tests finished..");
    }

}
