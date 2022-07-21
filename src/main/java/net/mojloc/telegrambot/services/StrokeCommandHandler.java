package net.mojloc.telegrambot.services;

import net.mojloc.telegrambot.model.Messages;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service("strokeCommandHandler")
public class StrokeCommandHandler extends CommandHandler{

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage) {
        SendMessage sendMessage = new SendMessage();
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
        }

        return sendMessage;
    }
}
