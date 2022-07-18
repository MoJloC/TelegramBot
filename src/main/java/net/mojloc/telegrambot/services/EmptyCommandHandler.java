package net.mojloc.telegrambot.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class EmptyCommandHandler extends CommandHandler{

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage) {
        strokeCount = 1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
        sendMessage.setText(incomingMessage.getText());

        return sendMessage;
    }
}
