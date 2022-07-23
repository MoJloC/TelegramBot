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

@Service("weatherForecastCommandHandler")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WeatherForecastCommandHandler extends CommandHandler{
    WeatherRequester weatherRequester;
    MessageParser messageParser;
    WeatherJsonParser weatherJsonParser;

    String command = Commands.WEATHER_FORECAST.getCommand();

    @Autowired
    public WeatherForecastCommandHandler(WeatherRequester weatherRequester, MessageParser messageParser,
                                         @Qualifier("weatherForecastJsonParser") WeatherJsonParser weatherJsonParser) {
        this.weatherRequester = weatherRequester;
        this.messageParser = messageParser;
        this.weatherJsonParser = weatherJsonParser;
    }

    @Override
    BotApiMethod<?> commandHandler(Message incomingMessage) {
        strokeCount=1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(incomingMessage.getChatId());
        List<String> attributes = messageParser.searchForCommandAttributes(incomingMessage.getText(), command);

        if (attributes.size()==0) {
            sendMessage.setText(Messages.NEED_CITY.getMessage());

            return sendMessage;
        }

        String cityName = attributes.get(0);

        try {
            log.info("Weather forecast request for City " + attributes.get(0) +
                    " from user " + incomingMessage.getFrom().getFirstName() +
                    " " + incomingMessage.getFrom().getLastName());

            String typeOfRequest = "forecast";
            ResponseEntity<String> responseFromWeatherProvider = weatherRequester.getWeather(cityName, typeOfRequest);
            Weather weatherForecast = weatherJsonParser.parseJson(responseFromWeatherProvider);
            sendMessage.setText(weatherForecast.prepareMessageText());

            log.info("Request completed successfully");
        } catch (JsonProcessingException e) {
            log.error("Error in JSON processing at WeatherRequester!", e);
            sendMessage.setText(Messages.PRINT_WEATHER_ERROR.getMessage());
            return sendMessage;
        } catch (HttpClientErrorException e) {
            log.warn("Request completed but there is no such city (" + attributes.get(0) + ")" +
                    " in weather provider database");
            sendMessage.setText("Вы ввели название города: " + attributes.get(0) +
                    ". " + Messages.WRONG_CITY_NAME.getMessage());
        }

        return sendMessage;
    }
}
