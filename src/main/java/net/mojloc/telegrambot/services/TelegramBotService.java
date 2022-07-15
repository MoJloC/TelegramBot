package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service("telegramBotService")
@FieldDefaults (makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBotService {
    MessageHandler messageHandler;

    @Autowired
    public TelegramBotService(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return null;
        } else {
            if (update.hasEditedMessage()) {
                return messageHandler.onMessageReceived(update.getEditedMessage());
            } else {
                return messageHandler.onMessageReceived(update.getMessage());
            }
        }
    }
}