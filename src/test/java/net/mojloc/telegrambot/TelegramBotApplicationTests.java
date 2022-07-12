package net.mojloc.telegrambot;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootTest
class TelegramBotApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(TelegramBotApplicationTests.class);
    @Autowired
    private ConfigurableApplicationContext context;

    @BeforeAll
    static void init() {
        logger.info("Tests started");
    }

    @Test
    void testContextLoads() {
        Assertions.assertTrue(context.isActive());
    }

    @AfterAll
    static void finish() {
        logger.info("Tests finished..");
    }

}
