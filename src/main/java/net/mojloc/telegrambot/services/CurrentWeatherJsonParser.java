package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.mojloc.telegrambot.model.CurrentWeather;
import net.mojloc.telegrambot.model.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("currentWeatherJsonParser")
public class CurrentWeatherJsonParser implements WeatherJsonParser {

    @Override
    public Weather parseJson(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        CurrentWeather currentWeather = new CurrentWeather();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
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
