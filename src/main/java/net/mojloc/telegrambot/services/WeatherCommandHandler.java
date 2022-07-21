package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.CurrentWeather;
import net.mojloc.telegrambot.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service("weatherCommandHandler")
@FieldDefaults (level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Slf4j
public class WeatherCommandHandler extends CommandHandler{
    WeatherRequester weatherRequester;
    String command = "/weather";

    @Autowired
    public WeatherCommandHandler(@Qualifier("weatherRequester") WeatherRequester weatherRequester) {
        this.weatherRequester = weatherRequester;
    }

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage) {
        strokeCount=1;
        MessageParser messageParser = new MessageParser();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(incomingMessage.getChatId());
        List<String> attributes = messageParser.searchForCommandAttributes(incomingMessage.getText(), command);

        if (attributes.size()==0) {
            sendMessage.setText(Messages.NEED_CITY.getMessage());

            return sendMessage;
        }

        try {
            log.info("Current weather request for City " + attributes.get(0) + " from user " + incomingMessage.getFrom());
            CurrentWeather currentWeather = weatherRequester.getCurrentWeather(attributes.get(0));
            log.info("Request completed successfully");
            sendMessage.setText(currentWeather.toString());
        } catch (JsonProcessingException e) {
            log.error("Error in JSON processing at WeatherRequester!", e);
            sendMessage.setText(Messages.PRINT_WEATHER_ERROR.getMessage());
            return sendMessage;
        } catch (HttpClientErrorException e) {
            log.info("Request completed but there is no such city (" + attributes.get(0) + ")" +
                     " in weather provider database");
            sendMessage.setText("Вы ввели: " + attributes.get(0) + ". " + Messages.WRONG_CITY_NAME);
        }

        return sendMessage;
    }
}
