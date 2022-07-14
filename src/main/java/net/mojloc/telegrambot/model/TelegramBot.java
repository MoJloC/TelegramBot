package net.mojloc.telegrambot.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.mojloc.telegrambot.services.TelegramBotService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
@FieldDefaults (level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botUsername;
    String botPath;
    String webhookPath;
    String botToken;
    TelegramBotService telegramBotService;

    public TelegramBot(SetWebhook setWebhook, TelegramBotService telegramBotService) {
        super(setWebhook);
        this.telegramBotService = telegramBotService;
    }

    public TelegramBot(DefaultBotOptions options, SetWebhook setWebhook, TelegramBotService telegramBotService) {
        super(options, setWebhook);
        this.telegramBotService = telegramBotService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramBotService.handleUpdate(update);
    }

}
