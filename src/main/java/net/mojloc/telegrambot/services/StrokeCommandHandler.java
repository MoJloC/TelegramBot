package net.mojloc.telegrambot.services;

import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.Messages;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("strokeCommandHandler")
@Slf4j
public class StrokeCommandHandler extends CommandHandler{

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage, String updateId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));

    // TODO: Change the syntax of switch command to Pattern-Matching if Java version of the app is changed to 14 and higher
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
        }

        log.info("Update ID " + updateId + ": message text contains the command /stroke."
                 + "A regular response has been sent.");

        return sendMessage;
    }
}
