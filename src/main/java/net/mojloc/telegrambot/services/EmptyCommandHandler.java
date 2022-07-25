package net.mojloc.telegrambot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("emptyResponseHandler")
@Slf4j
public class EmptyCommandHandler extends CommandHandler{

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage, String updateId) {
        strokeCount = 1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
        sendMessage.setText(incomingMessage.getText());

        log.info("Update ID " + updateId + ": message text doesn't contain any known commands."
                 + " Mirroring the text back ");

        return sendMessage;
    }
}
