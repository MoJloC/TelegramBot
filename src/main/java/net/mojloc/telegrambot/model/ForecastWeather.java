package net.mojloc.telegrambot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ForecastWeather implements Weather {

    @Override
    public String prepareMessageText() {
        return null;
    }
}
