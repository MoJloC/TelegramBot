package net.mojloc.telegrambot.Config;

import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.TelegramBot;
import net.mojloc.telegrambot.services.TelegramBotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@Slf4j
public class BotApplicationConfig {
    private final TelegramBotConfig telegramBotConfig;

    public BotApplicationConfig(TelegramBotConfig telegramBotConfig) {
        this.telegramBotConfig = telegramBotConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramBotConfig.getWebhookPath())
                                   .secretToken(telegramBotConfig.getSecretToken())
                                   .build();
    }

    @Bean
    public TelegramBot webhookBot (SetWebhook setWebhook, TelegramBotService telegramBotService) {
        TelegramBot webhookBot = new TelegramBot(setWebhook, telegramBotService);
        webhookBot.setBotUsername(telegramBotConfig.getUsername());
        webhookBot.setBotToken(telegramBotConfig.getToken());
        webhookBot.setBotPath(telegramBotConfig.getBotPath());
        try {
            webhookBot.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error("Can't set Webhook", e);
        }

        return webhookBot;
    }
}
