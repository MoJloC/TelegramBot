package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.InvocationTargetException;

@Component
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

        BotApiMethod<?> botApiMethod = null;

        try {
            botApiMethod = messageResponseHandler.messageResponse(incomingMessage);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException from messageResponseHandler.messageResponse(incomingMessage)", e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException from messageResponseHandler.messageResponse(incomingMessage)", e);
        } catch (InstantiationException e) {
            log.error("InstantiationException from messageResponseHandler.messageResponse(incomingMessage)", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException from messageResponseHandler.messageResponse(incomingMessage)", e);
        }

        return botApiMethod;
    }
}
