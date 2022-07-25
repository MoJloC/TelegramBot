package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service("weatherRequester")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WeatherRequester {
    String requestUrl;
    String apiKey;
    RestTemplate restTemplate = new RestTemplate();

    public WeatherRequester(@Value("${telegram.bot.weather.url}") String requestUrl,
                            @Value("${telegram.bot.weather.apikey}") String apiKey) {
        this.requestUrl = requestUrl;
        this.apiKey = apiKey;
    }

    public ResponseEntity<String> getWeather (String cityName, String typeOfRequest, String updateId) throws HttpClientErrorException {

        String url = requestUrl + typeOfRequest + "?q=" + cityName + "&lang=ru&appid=" + apiKey;

        log.info("Update ID " + updateId + ": sending request for current weather to " + requestUrl);
        ResponseEntity<String> weatherInJson = restTemplate.getForEntity(url, String.class);
        log.info("Update ID " + updateId + ": received response from " + requestUrl + "\n" + weatherInJson);

        return weatherInJson;
    }
}
