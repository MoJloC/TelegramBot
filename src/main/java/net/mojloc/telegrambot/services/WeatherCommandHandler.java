package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.Messages;
import net.mojloc.telegrambot.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service("weatherCommandHandler")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WeatherCommandHandler extends CommandHandler{
    WeatherRequester weatherRequester;
    MessageParser messageParser;
    WeatherJsonParser weatherJsonParser;

    String command = Commands.WEATHER.getCommand();

    @Autowired
    public WeatherCommandHandler(WeatherRequester weatherRequester, MessageParser messageParser,
                                 @Qualifier("currentWeatherJsonParser") WeatherJsonParser weatherJsonParser) {
        this.weatherRequester = weatherRequester;
        this.messageParser = messageParser;
        this.weatherJsonParser = weatherJsonParser;
    }

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage, String updateId) {
        strokeCount=1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(incomingMessage.getChatId());
        List<String> attributes = messageParser.searchForCommandAttributes(incomingMessage.getText(), command, 1);

        log.info("Update ID " + updateId + ": message text contains the command /weather from user "
                 + incomingMessage.getFrom().getFirstName()
                 + " " + incomingMessage.getFrom().getLastName()
                 + " (user_id: " + incomingMessage.getFrom().getId() + ") "
                 + ". Starting processing");

        if (attributes.size()==0) {
            sendMessage.setText(Messages.NEED_CITY.getMessage());
            log.info("Update ID " + updateId + ": weather request can't be completed due empty information about"
                     + " city name. Prepared regular notification for user");

            return sendMessage;
        }

        String cityName = attributes.get(0);

        try {
            log.info("Update ID " + updateId + ": prepared weather request for City " + attributes.get(0));

            String typeOfRequest = "weather";
            ResponseEntity<String> responseFromWeatherProvider = weatherRequester.getWeather(cityName, typeOfRequest,
                                                                                             updateId);
            Weather currentWeather = weatherJsonParser.parseJson(responseFromWeatherProvider);
            sendMessage.setText(currentWeather.prepareMessageText());

            log.info("Update ID " + updateId + ": weather request successfully completed and submitted"
                     + " for transmission to the user");

        } catch (JsonProcessingException e) {
            log.error("Update ID " + updateId + ": error in JSON processing at WeatherRequester!", e);
            sendMessage.setText(Messages.PRINT_WEATHER_ERROR.getMessage());
            return sendMessage;
        } catch (HttpClientErrorException e) {
            log.info("Update ID " + updateId + ": weather request completed but there is no such city ("
                     + attributes.get(0) + ")" + " in weather provider database. Prepared regular notification for user");
            sendMessage.setText("Вы ввели название города: " + attributes.get(0) +
                                ". " + Messages.WRONG_CITY_NAME.getMessage());
        }

        return sendMessage;
    }
}
