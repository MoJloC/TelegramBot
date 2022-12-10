package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Service("messageResponseHandler")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageResponseHandler {
    ConfigurableApplicationContext context;

    @Autowired
    public MessageResponseHandler(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public BotApiMethod<?> messageResponse (Message incomingMessage, String updateId) {
        Class<? extends CommandHandler> commandHandlerClass = detectionClassOfCommandHandler(incomingMessage.getText());
        CommandHandler commandHandler = context.getBean(commandHandlerClass);
        return commandHandler.commandHandler(incomingMessage, updateId);
    }

    private Class<? extends CommandHandler> detectionClassOfCommandHandler (String incomingMessageText) {
        Commands command = Arrays.stream(Commands.values())
                                        .filter((c) -> Arrays.asList(incomingMessageText.split(" "))
                                                       .contains(c.getCommand()))
                                        .findFirst()
                                        .orElse(Commands.EMPTY_COMMAND);

        return command.getCommandHandlerClass();
    }
}
