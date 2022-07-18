package net.mojloc.telegrambot.services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Component
public class MessageResponseHandler {

    public BotApiMethod<?> messageResponse (Message incomingMessage) throws NoSuchMethodException,
                                                                            InvocationTargetException,
                                                                            InstantiationException,
                                                                            IllegalAccessException {

        CommandHandler commandHandler = detectionClassOfCommandHandler(incomingMessage.getText())
                                                                      .getDeclaredConstructor()
                                                                      .newInstance();

        return commandHandler.commandHandler(incomingMessage);
    }

    private Class<? extends CommandHandler> detectionClassOfCommandHandler (String incomingMessageText) {

        Commands command = Arrays.stream(Commands.values()).filter((c) -> c.getCommand().equals(incomingMessageText))
                                                           .findFirst()
                                                           .orElse(Commands.EMPTY_COMMAND);


        return command.getCommandHandlerClass();
    }
}
