package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.Messages;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class MessageHandler {
    static int strokeCount = 1;

    public BotApiMethod<?> onMessageReceived(Message incomingMessage) {
        if (!incomingMessage.hasText()) {
            throw new IllegalArgumentException();
        }

        SendMessage sendMessage = new SendMessage();

        if (incomingMessage.getText().equals("/start")) {
            strokeCount = 1;
            sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
            sendMessage.setText(Messages.GREETING.getMessage());
            return sendMessage;
        }

        if (incomingMessage.getText().equals("/stroke")) {
            sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));

            switch (strokeCount) {
                case (1) :
                    sendMessage.setText(Messages.ON_STROKE1.getMessage());
                    break;
                case (2) :
                    sendMessage.setText(Messages.ON_STROKE2.getMessage());
                    break;
                case (3) :
                    sendMessage.setText(Messages.ON_STROKE3.getMessage());
                    break;
                default:
                    sendMessage.setText(Messages.ON_STROKE4.getMessage());
                    break;
            }

            if (strokeCount<= 3) {
                strokeCount++;
                log.info(String.valueOf(strokeCount));
            }

            return sendMessage;
        }

        strokeCount = 1;
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
        sendMessage.setText(incomingMessage.getText());

        return sendMessage;
    }
}
