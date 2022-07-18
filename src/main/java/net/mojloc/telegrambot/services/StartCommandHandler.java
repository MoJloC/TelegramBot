package net.mojloc.telegrambot.services;

import net.mojloc.telegrambot.model.Messages;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class StartCommandHandler extends CommandHandler{

    @Override
    public BotApiMethod<?> commandHandler(Message incomingMessage) {
        strokeCount=1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
        sendMessage.setText(Messages.GREETING.getMessage());
        return sendMessage;
    }
}
