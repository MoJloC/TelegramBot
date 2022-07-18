package net.mojloc.telegrambot.services;

public enum Commands {

    EMPTY_COMMAND("empty", EmptyCommandHandler.class),
    START("/start", StartCommandHandler.class),
    STROKE("/stroke", StrokeCommandHandler.class);

    private String command;
    private Class<? extends CommandHandler> commandHandlerClass;

    Commands(String command, Class<? extends CommandHandler> commandHandlerClass) {
        this.command = command;
        this.commandHandlerClass = commandHandlerClass;
    }

    public String getCommand() {
        return command;
    }

    public Class<? extends CommandHandler> getCommandHandlerClass() {
        return commandHandlerClass;
    }
}
