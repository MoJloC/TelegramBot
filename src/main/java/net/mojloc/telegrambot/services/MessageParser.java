package net.mojloc.telegrambot.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("messageParser")
@NoArgsConstructor
public class MessageParser {

    public List<String> searchForCommandAttributes (String messageText, String command, int limit) {

        return Arrays.stream(messageText.split(" ")).dropWhile((word) -> !word.equals(command))
                .dropWhile((w) -> w.equals(command))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
