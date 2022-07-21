package net.mojloc.telegrambot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeather implements Weather {
    int responseStatus;
    String cityName;
    int visibility;
    String description;
    Long temp;
    Long tempFeelsLike;
    int pressure;
    Long humidity;
    Long windSpeed;

    @Override
    public String prepareMessageText() {
        return ("Погода в городе " + getCityName() + ": " + getDescription() + "\n"
                + "Температура: " + (getTemp()-273) + " C (ощущается как " + (getTempFeelsLike()-273) + " C)\n"
                + "Давление: " + (getPressure()*0.75) + " мм Рт.ст.\n"
                + "Влажность: " + getHumidity() + "%\n"
                + "Скорость ветра: " + getWindSpeed() + " м/с\n"
                + "Дальность прямой видимости: " + getVisibility() + " м.");
    }
}



