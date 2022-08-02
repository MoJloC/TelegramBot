package net.mojloc.telegrambot.services;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {
    String messageText = "Шуша /start Томск";
    String messageText1 = "/weather";
    String command = "/start";
    String command1 = "/weather";
    MessageParser messageParser = new MessageParser();

    @Test
    public void testSearchForCommandAttributes() {
        List<String> commandAttributes = messageParser.searchForCommandAttributes(messageText, command, 1);
        System.out.println(commandAttributes);
        assertEquals(1, commandAttributes.size());
        assertEquals("Томск", commandAttributes.get(0));
    }

    @Test
    public void testSearchForCommandAttributes2() {
        List<String> commandAttributes = messageParser.searchForCommandAttributes(messageText1, command1, 1);
        System.out.println(commandAttributes);
        assertEquals(0, commandAttributes.size());
    }
}