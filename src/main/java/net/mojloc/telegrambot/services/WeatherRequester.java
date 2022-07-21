package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.CurrentWeather;
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

    public CurrentWeather getCurrentWeather (String cityName) throws JsonProcessingException, HttpClientErrorException {
        CurrentWeather currentWeather = new CurrentWeather();
        String url = new StringBuilder().append(requestUrl)
                                        .append("weather?q=")
                                        .append(cityName)
                                        .append("&lang=ru&appid=")
                                        .append(apiKey)
                                        .toString();

        log.info("Sending request for current weather to " + requestUrl);
        ResponseEntity<String> weatherInJson = restTemplate.getForEntity(url, String.class);
        log.info("Received response from " + requestUrl + "\n" + weatherInJson);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(weatherInJson.getBody());
        ArrayNode jsonWeatherArray = (ArrayNode) rootNode.get("weather");
        currentWeather.setResponseStatus(rootNode.path("cod").asInt());
        currentWeather.setCityName(rootNode.path("name").asText());
        currentWeather.setDescription(jsonWeatherArray.get(0).path("description").asText());
        currentWeather.setTemp(rootNode.path("main").path("temp").asLong());
        currentWeather.setTempFeelsLike(rootNode.path("main").path("feels_like").asLong());
        currentWeather.setPressure(rootNode.path("main").path("pressure").asInt());
        currentWeather.setHumidity(rootNode.path("main").path("humidity").asLong());
        currentWeather.setVisibility(rootNode.path("visibility").asInt());
        currentWeather.setWindSpeed(rootNode.path("wind").path("speed").asLong());

        return currentWeather;

    }
}
