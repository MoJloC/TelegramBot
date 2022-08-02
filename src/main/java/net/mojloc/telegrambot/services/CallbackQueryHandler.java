package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Arrays;

@Service("callbackQueryHandler")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CallbackQueryHandler {
    QuizCommandHandler quizCommandHandler;

    @Autowired
    public CallbackQueryHandler(QuizCommandHandler quizCommandHandler) {
        this.quizCommandHandler = quizCommandHandler;
    }

    public BotApiMethod<?> handleCallbackQuery(CallbackQuery callbackQuery, String updateId) {
        String[] callbackQueryDataArray = callbackQuery.getData().split(" ");
        String callbackQueryMarker = callbackQueryDataArray[0];

        if (callbackQueryMarker.equals("Quiz")) {
            log.info("Update ID " + updateId + ": callbackQuery contains answer for Quiz question from user with Id "
                    + callbackQuery.getFrom().getId());

            return quizCommandHandler.callbackQueryHandler(callbackQuery, updateId);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(callbackQuery.getMessage().getChatId());
            sendMessage.setText("Мя... Что-то пошло совсем не так. Мы обязательно с этим разберёмся в ближайшее время!");

            log.warn("Update ID " + updateId + ": can't handle CallbackQuery data at handleCallbackQuery. You should fix it!");

            return null;
        }
    }
}
