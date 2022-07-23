package net.mojloc.telegrambot.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListOfDatesAndTimeStampsProvider {

    public List<String> provideList() {
        List<String> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDate.now().atTime(00,00,00);
        LocalDateTime dateTime1 = LocalDate.now().atTime(06,00,00);
        LocalDateTime dateTime2 = LocalDate.now().atTime(12,00,00);

        for (int i = 1; i<=3; i++) {
            result.add(dateTime.plusDays(i).format(formatter));
            result.add(dateTime1.plusDays(i).format(formatter));
            result.add(dateTime2.plusDays(i).format(formatter));
        }

        return result;
    }
}
