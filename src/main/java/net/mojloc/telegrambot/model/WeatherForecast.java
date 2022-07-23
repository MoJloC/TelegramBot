package net.mojloc.telegrambot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class WeatherForecast implements Weather {
    int responseStatus;
    String cityName;
    List<String> visibility = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<Integer> temp = new ArrayList<>();
    List<Integer> tempFeelsLike = new ArrayList<>();
    List<Integer> pressure = new ArrayList<>();
    List<Integer> humidity = new ArrayList<>();
    List<Integer> probabilityOfPrecipitation = new ArrayList<>();
    List<String> windSpeed = new ArrayList<>();
    List<String> dates = new ArrayList<>();


    @Override
    public String prepareMessageText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy г.");
        LocalDate currentDate = LocalDate.now();
        for (int i = 1; i <= 3; i++){
            dates.add(currentDate.plusDays(i).format(formatter));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Погода в городе ");
        sb.append(getCityName());
        sb.append(" на три дня:\n\n");

        for (int i = 2, j = 0; i <= 8; i+=3 , j++) {
            sb.append(getDates().get(j));
            sb.append("\n");
            sb.append("Общее состояние - у: ");
            sb.append(getDescription().get(i-2));
            sb.append(", д: ");
            sb.append(getDescription().get(i-1));
            sb.append(", в: ");
            sb.append(getDescription().get(i));
            sb.append("\n");
            sb.append("Температура, С - у: ");
            sb.append(getTemp().get(i-2));
            sb.append("(");
            sb.append(getTempFeelsLike().get(i-2));
            sb.append(")");
            sb.append(", д: ");
            sb.append(getTemp().get(i-1));
            sb.append("(");
            sb.append(getTempFeelsLike().get(i-1));
            sb.append(")");
            sb.append(", в: ");
            sb.append(getTemp().get(i));
            sb.append("(");
            sb.append(getTempFeelsLike().get(i));
            sb.append(")");
            sb.append("\n");
            sb.append("Скорость ветра, м/с - у: ");
            sb.append(getWindSpeed().get(i-2));
            sb.append(", д: ");
            sb.append(getWindSpeed().get(i-1));
            sb.append(", в: ");
            sb.append(getWindSpeed().get(i));
            sb.append("\n");
            sb.append("Давление, мм.РТ.ст. - у: ");
            sb.append(getPressure().get(i-2)*0.75);
            sb.append(", д: ");
            sb.append(getPressure().get(i-1)*0.75);
            sb.append(", в: ");
            sb.append(getPressure().get(i)*0.75);
            sb.append("\n");
            sb.append("Влажность, % - у: ");
            sb.append(getHumidity().get(i-2));
            sb.append(", д: ");
            sb.append(getHumidity().get(i-1));
            sb.append(", в: ");
            sb.append(getHumidity().get(i));
            sb.append("\n");
            sb.append("Вер-сть осадков, % - у: ");
            sb.append(getProbabilityOfPrecipitation().get(i-2));
            sb.append(", д: ");
            sb.append(getProbabilityOfPrecipitation().get(i-1));
            sb.append(", в: ");
            sb.append(getProbabilityOfPrecipitation().get(i));
            sb.append("\n\n");
        }
        sb.append("Где: \n у: 06:00, д: 13:00, в: 19:00 по Томскому времени. У температуры в скобках указана температура" +
                  " типа \"ощущается\"\n");

        return sb.toString();
    }
}
