package net.mojloc.telegrambot.model;

public enum Messages {
    GREETING("Мяу! Меня зовут Шуша. Буду рада Вам помочь!"),
    ON_STROKE1("Мурррр!"),
    ON_STROKE2("Мурррр-мурррр!"),
    ON_STROKE3("Мурррр! И за ушками тоже!"),
    ON_STROKE4("Мурррррррррр-мурррррррррр!"),
    PRINT_WEATHER_SUCCESS("Мяф! Вот, что цепкий коготочек поймал: "),
    PRINT_WEATHER_ERROR("Искала-искала, царапала-царапала, но нет ничего. Такое вот мяу. Мы с создателем " +
                                "обязательно решим эту проблему чуть позже. Я ему уже мяукнула"),
    NEED_CITY("Не могу спросить погоду, не зная города. Напишите, пожалуйста, его через пробел после команды"),
    WRONG_CITY_NAME("Информер не может найти такое имя города у себя в базе. Проверьте, пожалуйста, корректность" +
                            " написания названия города и я уверена, что смогу помочь.");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
