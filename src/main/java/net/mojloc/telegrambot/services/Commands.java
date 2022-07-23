package net.mojloc.telegrambot.services;

public enum Commands {

    EMPTY_COMMAND("empty", EmptyCommandHandler.class),
    START("/start", StartCommandHandler.class),
    STROKE("/stroke", StrokeCommandHandler.class),
    WEATHER("/weather", WeatherCommandHandler.class),
    WEATHER_FORECAST("/weatherf", WeatherForecastCommandHandler.class);

    private final String command;
    private final Class<? extends CommandHandler> commandHandlerClass;

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

/*
For list of commands at @BotFather

start - поздороваться с Шушей
stroke - погладить Шушу
weather - погода сейчас (название города через пробел)
weatherf - прогноз погоды на ближайшие 3 дня

*/
