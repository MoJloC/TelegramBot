package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.mojloc.telegrambot.model.Weather;
import net.mojloc.telegrambot.model.WeatherForecast;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("weatherForecastJsonParser")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastJsonParser implements WeatherJsonParser {

    @Override
    public Weather parseJson(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        List<String> datesAndTimeStamps=new ListOfDatesAndTimeStampsProvider().provideList();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

        WeatherForecast weatherForecast = new WeatherForecast();
        weatherForecast.setResponseStatus(rootNode.path("code").asInt());
        weatherForecast.setCityName(rootNode.path("city").path("name").asText());

        ArrayNode innerList = (ArrayNode) rootNode.get("list");
        ArrayList<Integer> arrayForIndexes = new ArrayList<>();
        Iterator<JsonNode> jsonArrayIterator = innerList.iterator();
        int tempCounter = 0;

        while (jsonArrayIterator.hasNext()) {
            String jsonNodeDttxt = jsonArrayIterator.next().path("dt_txt").asText();
            if (datesAndTimeStamps.contains(jsonNodeDttxt)) {
                arrayForIndexes.add(tempCounter);
            }
            tempCounter++;
        }

        Iterator<Integer> iterator = arrayForIndexes.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            weatherForecast.getVisibility().add(innerList.get(index).path("visibility").asText());
            weatherForecast.getTemp().add(innerList.get(index).path("main").path("temp").asInt()-273);
            weatherForecast.getTempFeelsLike().add(innerList.get(index).path("main").path("feels_like").asInt()-273);
            weatherForecast.getPressure().add(innerList.get(index).path("main").path("pressure").asInt());
            weatherForecast.getHumidity().add(innerList.get(index).path("main").path("humidity").asInt());
            weatherForecast.getProbabilityOfPrecipitation().add(innerList.get(index).path("pop").asInt()*100);
            weatherForecast.getWindSpeed().add(innerList.get(index).path("wind").path("speed").asText());
            weatherForecast.getDescription().add(innerList.get(index).get("weather")
                                                                     .get(0).path("description").asText());
        }

        return weatherForecast;
    }
}
