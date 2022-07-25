package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    public BotApiMethod<?> onMessageReceived(Message incomingMessage, String updateId) {
        if (!incomingMessage.hasText()) {
            log.warn("Update ID " + updateId + ": detecting empty message text error in request from user "
                    + incomingMessage.getFrom().getFirstName() + " " + incomingMessage.getFrom().getLastName()
            + " with nickname " + incomingMessage.getFrom().getUserName());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(incomingMessage.getChatId());
            sendMessage.setText("Что-то пошло не так и мы получили пустую строку сообщения." +
                                " Попробуйте ещё раз набрать команду");
            return sendMessage;
        }

        BotApiMethod<?> botApiMethod = messageResponseHandler.messageResponse(incomingMessage, updateId);

        return botApiMethod;
    }
}
