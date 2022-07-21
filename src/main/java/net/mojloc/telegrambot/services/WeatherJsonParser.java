package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.mojloc.telegrambot.model.Weather;
import org.springframework.http.ResponseEntity;

public interface WeatherJsonParser {
    Weather parseJson(ResponseEntity<String> responseEntity) throws JsonProcessingException;
}
