package net.mojloc.telegrambot.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class CommandHandler {
    static int strokeCount = 1;

    abstract BotApiMethod<?> commandHandler(Message incomingMessage, String updateId);
}
