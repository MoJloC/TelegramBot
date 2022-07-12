package net.mojloc.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication {

    private static Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        SpringApplication.run(TelegramBotApplication.class, args);
        logger.info("Application started");
    }
}
