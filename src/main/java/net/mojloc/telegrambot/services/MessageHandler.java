package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.InvocationTargetException;

@Service("messageHandler")
@FieldDefaults (makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class MessageHandler {
    MessageResponseHandler messageResponseHandler;

    @Autowired
    public MessageHandler(MessageResponseHandler messageResponseHandler) {
        this.messageResponseHandler = messageResponseHandler;
    }

    public BotApiMethod<?> onMessageReceived(Message incomingMessage) {
        if (!incomingMessage.hasText()) {
            throw new IllegalArgumentException();
        }

        BotApiMethod<?> botApiMethod = messageResponseHandler.messageResponse(incomingMessage);

        return botApiMethod;
    }
}
