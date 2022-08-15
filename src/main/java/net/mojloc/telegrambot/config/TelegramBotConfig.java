package net.mojloc.telegrambot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("telegramBotConfig")
@Getter
@FieldDefaults (level = AccessLevel.PRIVATE)
public class TelegramBotConfig {
    @Value("${bot.username}")
    String username;
    @Value("${bot.token}")
    String token;
    @Value("${telegram.webhook.path}")
    String webhookPath;
    @Value("${telegram.bot.path}")
    String botPath;
    @Value("${telegram.webhook.secret.token}")
    String secretToken;
}