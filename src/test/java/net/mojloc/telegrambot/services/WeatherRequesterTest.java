package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.CurrentWeather;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
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
        CurrentWeather currentWeather = null;
        try {
            currentWeather = weatherRequester.getCurrentWeather(cityName);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing JSON", e);
        } catch (HttpClientErrorException e) {
            log.error("Error: City " + cityName + " not found by weather informer!", e);
        }
        assertEquals(200, currentWeather.getResponseStatus());
        assertEquals(cityName, currentWeather.getCityName());
        log.info(currentWeather.toString());
    }
}