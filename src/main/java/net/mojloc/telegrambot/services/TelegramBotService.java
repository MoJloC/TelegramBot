package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service("telegramBotService")
@FieldDefaults (makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBotService {
    MessageHandler messageHandler;

    @Autowired
    public TelegramBotService(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        log.debug("Has Message: " + update.hasMessage());
        log.debug("Has EditedMessage: " + update.hasEditedMessage());
        log.debug("Has CallbackQuery: " + update.hasCallbackQuery());
        log.debug("Has InlineQuery: " + update.hasInlineQuery());
        log.debug("Has ChosenInlineQuery: " + update.hasChosenInlineQuery());
        log.debug("Has ShippingQuery: " + update.hasShippingQuery());
        log.debug("Has PreCheckoutQuery: " + update.hasPreCheckoutQuery());
        log.debug("Has Poll: " + update.hasPoll());
        log.debug("Has PollAnswer: " + update.hasPollAnswer());
        log.debug("Has ChannelPost: " + update.hasChannelPost());
        log.debug("Has ChatJoinRequest: " + update.hasChatJoinRequest());
        log.debug("Has EditedChannelPost: " + update.hasEditedChannelPost());
        log.debug("Has ChatMember: " + update.hasChatMember());
        log.debug("Has MyChatMember: " + update.hasMyChatMember());

        if (update.hasCallbackQuery()) {
            log.info("Update ID " + update.getUpdateId() + ": contains CallbackQuery object");
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return null;

        } else if (update.hasMyChatMember()) {
            log.info("Update ID " + update.getUpdateId() + ": contains MyChatMember information");
            log.info("Shusha's chat member status has updates in chat: " + update.getMyChatMember().getChat().getDescription()
                     + " by user" + update.getMyChatMember().getFrom().getFirstName()
                     + " " + update.getMyChatMember().getFrom().getLastName()
                     + " ("+ update.getMyChatMember().getFrom().getUserName()
                     + ")\nfrom: " + update.getMyChatMember().getOldChatMember()
                     + "\nto: " + update.getMyChatMember().getNewChatMember());
            return null;

        } else if (update.hasMessage() ) {
            log.info("Update ID " + update.getUpdateId() + ": contains Message object");
            return messageHandler.onMessageReceived(update.getMessage(), String.valueOf(update.getUpdateId()));

        } else if (update.hasEditedMessage()) {
            log.info("Update ID " + update.getUpdateId() + ": contains EditedMessage object");
            return messageHandler.onMessageReceived(update.getEditedMessage(), String.valueOf(update.getUpdateId()));

        } else {
            log.warn("Update ID " + update.getUpdateId() + ": Attention! Unhandled variant of update has detected. "
                     + "You should to fix that.");
            return null;
        }
    }
}