package net.mojloc.telegrambot.services;

import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MessageParser {

    public List<String> searchForCommandAttributes (String messageText, String command) {

        return Arrays.stream(messageText.split(" ")).dropWhile((word) -> !word.equals(command))
                .dropWhile((w) -> w.equals(command))
                .limit(3)
                .collect(Collectors.toList());
    }
}
