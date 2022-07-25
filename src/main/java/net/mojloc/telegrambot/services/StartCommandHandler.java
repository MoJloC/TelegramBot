package net.mojloc.telegrambot.services;

import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.Messages;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("startCommandHandler")
@Slf4j
public class StartCommandHandler extends CommandHandler{

    @Override
    public BotApiMethod<?> commandHandler(Message incomingMessage, String updateId) {
        strokeCount=1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
        sendMessage.setText(Messages.GREETING.getMessage());

        log.info("Update ID " + updateId + ": message text contains the command /start."
                  + "A regular response has been sent.");

        return sendMessage;
    }
}
