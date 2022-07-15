package net.mojloc.telegrambot.model;

public enum Messages {
    GREETING("Мяу! Меня зовут Шуша. Буду рада Вам помочь!"),
    ON_STROKE1("Мурррр!"),
    ON_STROKE2("Мурррр-мурррр!"),
    ON_STROKE3("Мурррр! И за ушками тоже!"),
    ON_STROKE4("Мурррррррррр-мурррррррррр!"),
    PRINT_WEATHER_SUCCESS("Мяф! Вот, что цепкий коготочек поймал: "),
    PRINT_WEATHER_ERROR("Искала, царапала, но нет ничего. Такое вот мяу..");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
