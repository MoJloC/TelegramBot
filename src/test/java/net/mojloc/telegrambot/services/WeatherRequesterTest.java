package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.CurrentWeather;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class WeatherRequesterTest {
    WeatherRequester weatherRequester;

    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    void testGetCurrentWeather() {
        weatherRequester = context.getBean(WeatherRequester.class);
        String cityName = "Томск";
        String typeOfRequest = "weather";
        try {
            ResponseEntity<String> responseFromWeatherProvider = weatherRequester.getWeather(cityName, typeOfRequest);
            assertEquals("200 OK", responseFromWeatherProvider.getStatusCode().toString());
        } catch (HttpClientErrorException e) {
            log.error("Error: City " + cityName + " not found by weather informer!", e);
        }
    }

    @Test
    void testGetWeatherForecast() {
        weatherRequester = context.getBean(WeatherRequester.class);
        String cityName = "Анапа";
        String typeOfRequest = "forecast";
        try {
            ResponseEntity<String> responseFromWeatherProvider = weatherRequester.getWeather(cityName, typeOfRequest);
            assertEquals("200 OK", responseFromWeatherProvider.getStatusCode().toString());
        } catch (HttpClientErrorException e) {
            log.error("Error: City " + cityName + " not found by weather informer!", e);
        }
    }
}