package net.mojloc.telegrambot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.mojloc.telegrambot.model.WeatherForecast;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherForecastJsonParserTest {
    String weatherResponse = "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[" +
            "{\"dt\":1658480400,\"main\":{\"temp\":296.6,\"feels_like\":296.59,\"temp_min\":296.6,\"temp_max\":297.53,\"pressure\":1010,\"sea_level\":1010,\"grnd_level\":1010,\"humidity\":61,\"temp_kf\":-0.93},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":20},\"wind\":{\"speed\":6.16,\"deg\":248,\"gust\":7.23},\"visibility\":10000,\"pop\":0.36,\"rain\":{\"3h\":0.28},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-22 09:00:00\"}," +
            "{\"dt\":1658491200,\"main\":{\"temp\":297.12,\"feels_like\":297.14,\"temp_min\":297.12,\"temp_max\":297.61,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":60,\"temp_kf\":-0.49},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":32},\"wind\":{\"speed\":7.18,\"deg\":248,\"gust\":8.79},\"visibility\":10000,\"pop\":0.25,\"rain\":{\"3h\":0.16},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-22 12:00:00\"}," +
            "{\"dt\":1658502000,\"main\":{\"temp\":297.27,\"feels_like\":297.33,\"temp_min\":297.27,\"temp_max\":297.27,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":57},\"wind\":{\"speed\":5.84,\"deg\":253,\"gust\":7.58},\"visibility\":10000,\"pop\":0.82,\"rain\":{\"3h\":0.62},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-22 15:00:00\"}," +
            "{\"dt\":1658512800,\"main\":{\"temp\":295.62,\"feels_like\":295.8,\"temp_min\":295.62,\"temp_max\":295.62,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1009,\"humidity\":72,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"переменная облачность\",\"icon\":\"03n\"}],\"clouds\":{\"all\":49},\"wind\":{\"speed\":4.48,\"deg\":205,\"gust\":6.29},\"visibility\":10000,\"pop\":0.69,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-22 18:00:00\"}," +
            "{\"dt\":1658523600,\"main\":{\"temp\":295.21,\"feels_like\":295.46,\"temp_min\":295.21,\"temp_max\":295.21,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":76,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10n\"}],\"clouds\":{\"all\":4},\"wind\":{\"speed\":3.84,\"deg\":234,\"gust\":5.77},\"visibility\":10000,\"pop\":0.54,\"rain\":{\"3h\":0.34},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-22 21:00:00\"}," +
            "{\"dt\":1658534400,\"main\":{\"temp\":294.88,\"feels_like\":295.04,\"temp_min\":294.88,\"temp_max\":294.88,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1009,\"humidity\":74,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10n\"}],\"clouds\":{\"all\":28},\"wind\":{\"speed\":2.46,\"deg\":313,\"gust\":3.51},\"visibility\":10000,\"pop\":0.58,\"rain\":{\"3h\":0.72},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-23 00:00:00\"}," +
            "{\"dt\":1658545200,\"main\":{\"temp\":294.3,\"feels_like\":294.38,\"temp_min\":294.3,\"temp_max\":294.3,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":38},\"wind\":{\"speed\":3.45,\"deg\":336,\"gust\":4.84},\"visibility\":10000,\"pop\":0.73,\"rain\":{\"3h\":0.5},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-23 03:00:00\"}," +
            "{\"dt\":1658556000,\"main\":{\"temp\":295.33,\"feels_like\":295.41,\"temp_min\":295.33,\"temp_max\":295.33,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1010,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":21},\"wind\":{\"speed\":6.11,\"deg\":316,\"gust\":7.26},\"visibility\":10000,\"pop\":0.81,\"rain\":{\"3h\":1.46},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-23 06:00:00\"}," +
            "{\"dt\":1658566800,\"main\":{\"temp\":296.95,\"feels_like\":296.85,\"temp_min\":296.95,\"temp_max\":296.95,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1011,\"humidity\":56,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":3},\"wind\":{\"speed\":7.38,\"deg\":313,\"gust\":8.22},\"visibility\":10000,\"pop\":0.2,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-23 09:00:00\"}," +
            "{\"dt\":1658577600,\"main\":{\"temp\":297.22,\"feels_like\":297.09,\"temp_min\":297.22,\"temp_max\":297.22,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":3},\"wind\":{\"speed\":7.14,\"deg\":319,\"gust\":8.28},\"visibility\":10000,\"pop\":0.04,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-23 12:00:00\"}," +
            "{\"dt\":1658588400,\"main\":{\"temp\":297.69,\"feels_like\":297.56,\"temp_min\":297.69,\"temp_max\":297.69,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1011,\"humidity\":52,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.98,\"deg\":310,\"gust\":5.44},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-23 15:00:00\"}," +
            "{\"dt\":1658599200,\"main\":{\"temp\":295.12,\"feels_like\":295.1,\"temp_min\":295.12,\"temp_max\":295.12,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":66,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.26,\"deg\":230,\"gust\":3.41},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-23 18:00:00\"}," +
            "{\"dt\":1658610000,\"main\":{\"temp\":295.22,\"feels_like\":295.39,\"temp_min\":295.22,\"temp_max\":295.22,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10n\"}],\"clouds\":{\"all\":16},\"wind\":{\"speed\":5.06,\"deg\":273,\"gust\":6.91},\"visibility\":10000,\"pop\":0.35,\"rain\":{\"3h\":0.26},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-23 21:00:00\"}," +
            "{\"dt\":1658620800,\"main\":{\"temp\":294.98,\"feels_like\":295.18,\"temp_min\":294.98,\"temp_max\":294.98,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10n\"}],\"clouds\":{\"all\":8},\"wind\":{\"speed\":4.63,\"deg\":302,\"gust\":5.99},\"visibility\":10000,\"pop\":0.23,\"rain\":{\"3h\":0.62},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-24 00:00:00\"}," +
            "{\"dt\":1658631600,\"main\":{\"temp\":295.18,\"feels_like\":295.29,\"temp_min\":295.18,\"temp_max\":295.18,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":71,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":3.54,\"deg\":335,\"gust\":5.2},\"visibility\":10000,\"pop\":0.4,\"rain\":{\"3h\":0.13},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-24 03:00:00\"}," +
            "{\"dt\":1658642400,\"main\":{\"temp\":297.96,\"feels_like\":298.04,\"temp_min\":297.96,\"temp_max\":297.96,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":59,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.61,\"deg\":338,\"gust\":5.78},\"visibility\":10000,\"pop\":0.34,\"rain\":{\"3h\":0.15},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-24 06:00:00\"}," +
            "{\"dt\":1658653200,\"main\":{\"temp\":299.44,\"feels_like\":299.44,\"temp_min\":299.44,\"temp_max\":299.44,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1011,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":5.46,\"deg\":308,\"gust\":6.03},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-24 09:00:00\"}," +
            "{\"dt\":1658664000,\"main\":{\"temp\":300.03,\"feels_like\":300.51,\"temp_min\":300.03,\"temp_max\":300.03,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1010,\"humidity\":51,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":6.16,\"deg\":316,\"gust\":7.11},\"visibility\":10000,\"pop\":0.01,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-24 12:00:00\"}," +
            "{\"dt\":1658674800,\"main\":{\"temp\":298.82,\"feels_like\":298.96,\"temp_min\":298.82,\"temp_max\":298.82,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":58,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":5.62,\"deg\":273,\"gust\":6.29},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-24 15:00:00\"}," +
            "{\"dt\":1658685600,\"main\":{\"temp\":295.94,\"feels_like\":296.29,\"temp_min\":295.94,\"temp_max\":295.94,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1010,\"humidity\":77,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.77,\"deg\":257,\"gust\":5.2},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-24 18:00:00\"}," +
            "{\"dt\":1658696400,\"main\":{\"temp\":295.66,\"feels_like\":296.06,\"temp_min\":295.66,\"temp_max\":295.66,\"pressure\":1012,\"sea_level\":1012,\"grnd_level\":1010,\"humidity\":80,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":1},\"wind\":{\"speed\":2.48,\"deg\":265,\"gust\":3.39},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-24 21:00:00\"}," +
            "{\"dt\":1658707200,\"main\":{\"temp\":295.44,\"feels_like\":295.84,\"temp_min\":295.44,\"temp_max\":295.44,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":81,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":5},\"wind\":{\"speed\":2.14,\"deg\":237,\"gust\":2.79},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-25 00:00:00\"}," +
            "{\"dt\":1658718000,\"main\":{\"temp\":295.59,\"feels_like\":295.98,\"temp_min\":295.59,\"temp_max\":295.59,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":80,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":7},\"wind\":{\"speed\":3.43,\"deg\":206,\"gust\":4.62},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-25 03:00:00\"}," +
            "{\"dt\":1658728800,\"main\":{\"temp\":298.18,\"feels_like\":298.46,\"temp_min\":298.18,\"temp_max\":298.18,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1009,\"humidity\":66,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":5},\"wind\":{\"speed\":5.46,\"deg\":207,\"gust\":6},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-25 06:00:00\"}," +
            "{\"dt\":1658739600,\"main\":{\"temp\":298.78,\"feels_like\":299.1,\"temp_min\":298.78,\"temp_max\":298.78,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1010,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":4},\"wind\":{\"speed\":5.98,\"deg\":220,\"gust\":6.59},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-25 09:00:00\"}," +
            "{\"dt\":1658750400,\"main\":{\"temp\":298.81,\"feels_like\":299.13,\"temp_min\":298.81,\"temp_max\":298.81,\"pressure\":1010,\"sea_level\":1010,\"grnd_level\":1009,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"небольшая облачность\",\"icon\":\"02d\"}],\"clouds\":{\"all\":17},\"wind\":{\"speed\":6.68,\"deg\":207,\"gust\":7.48},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-25 12:00:00\"}," +
            "{\"dt\":1658761200,\"main\":{\"temp\":297.78,\"feels_like\":298.13,\"temp_min\":297.78,\"temp_max\":297.78,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1008,\"humidity\":70,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"облачно с прояснениями\",\"icon\":\"04d\"}],\"clouds\":{\"all\":69},\"wind\":{\"speed\":6.73,\"deg\":201,\"gust\":8.53},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-25 15:00:00\"}," +
            "{\"dt\":1658772000,\"main\":{\"temp\":296.63,\"feels_like\":297.12,\"temp_min\":296.63,\"temp_max\":296.63,\"pressure\":1010,\"sea_level\":1010,\"grnd_level\":1008,\"humidity\":80,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"облачно с прояснениями\",\"icon\":\"04n\"}],\"clouds\":{\"all\":84},\"wind\":{\"speed\":5.58,\"deg\":202,\"gust\":8.3},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-25 18:00:00\"}," +
            "{\"dt\":1658782800,\"main\":{\"temp\":296.06,\"feels_like\":296.57,\"temp_min\":296.06,\"temp_max\":296.06,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1007,\"humidity\":83,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"переменная облачность\",\"icon\":\"03n\"}],\"clouds\":{\"all\":43},\"wind\":{\"speed\":6.5,\"deg\":206,\"gust\":9.61},\"visibility\":10000,\"pop\":0.12,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-25 21:00:00\"}," +
            "{\"dt\":1658793600,\"main\":{\"temp\":296.24,\"feels_like\":296.67,\"temp_min\":296.24,\"temp_max\":296.24,\"pressure\":1008,\"sea_level\":1008,\"grnd_level\":1007,\"humidity\":79,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10n\"}],\"clouds\":{\"all\":46},\"wind\":{\"speed\":6.1,\"deg\":228,\"gust\":8.81},\"visibility\":10000,\"pop\":0.56,\"rain\":{\"3h\":1.33},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-26 00:00:00\"}," +
            "{\"dt\":1658804400,\"main\":{\"temp\":296.1,\"feels_like\":296.51,\"temp_min\":296.1,\"temp_max\":296.1,\"pressure\":1008,\"sea_level\":1008,\"grnd_level\":1007,\"humidity\":79,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":70},\"wind\":{\"speed\":2.45,\"deg\":232,\"gust\":3.03},\"visibility\":10000,\"pop\":0.72,\"rain\":{\"3h\":0.72},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-26 03:00:00\"}," +
            "{\"dt\":1658815200,\"main\":{\"temp\":298.35,\"feels_like\":298.62,\"temp_min\":298.35,\"temp_max\":298.35,\"pressure\":1008,\"sea_level\":1008,\"grnd_level\":1007,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":43},\"wind\":{\"speed\":4.28,\"deg\":212,\"gust\":4.74},\"visibility\":10000,\"pop\":0.72,\"rain\":{\"3h\":0.62},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-26 06:00:00\"}," +
            "{\"dt\":1658826000,\"main\":{\"temp\":299.11,\"feels_like\":299.11,\"temp_min\":299.11,\"temp_max\":299.11,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1007,\"humidity\":60,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.98,\"deg\":220,\"gust\":5.51},\"visibility\":10000,\"pop\":0.44,\"rain\":{\"3h\":0.19},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-26 09:00:00\"}," +
            "{\"dt\":1658836800,\"main\":{\"temp\":299.35,\"feels_like\":299.35,\"temp_min\":299.35,\"temp_max\":299.35,\"pressure\":1008,\"sea_level\":1008,\"grnd_level\":1007,\"humidity\":59,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"небольшой дождь\",\"icon\":\"10d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":5.02,\"deg\":227,\"gust\":5.57},\"visibility\":10000,\"pop\":0.51,\"rain\":{\"3h\":0.33},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-26 12:00:00\"}," +
            "{\"dt\":1658847600,\"main\":{\"temp\":298.8,\"feels_like\":298.99,\"temp_min\":298.8,\"temp_max\":298.8,\"pressure\":1008,\"sea_level\":1008,\"grnd_level\":1007,\"humidity\":60,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":2},\"wind\":{\"speed\":3.21,\"deg\":238,\"gust\":3.18},\"visibility\":10000,\"pop\":0.22,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-26 15:00:00\"}," +
            "{\"dt\":1658858400,\"main\":{\"temp\":296.26,\"feels_like\":296.53,\"temp_min\":296.26,\"temp_max\":296.26,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1007,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":2},\"wind\":{\"speed\":3.78,\"deg\":176,\"gust\":4.29},\"visibility\":10000,\"pop\":0.11,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-26 18:00:00\"}," +
            "{\"dt\":1658869200,\"main\":{\"temp\":295.41,\"feels_like\":295.83,\"temp_min\":295.41,\"temp_max\":295.41,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1007,\"humidity\":82,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.28,\"deg\":163,\"gust\":5.72},\"visibility\":10000,\"pop\":0.02,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-26 21:00:00\"}," +
            "{\"dt\":1658880000,\"main\":{\"temp\":295.08,\"feels_like\":295.5,\"temp_min\":295.08,\"temp_max\":295.08,\"pressure\":1009,\"sea_level\":1009,\"grnd_level\":1008,\"humidity\":83,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.76,\"deg\":155,\"gust\":4.94},\"visibility\":10000,\"pop\":0.02,\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2022-07-27 00:00:00\"}," +
            "{\"dt\":1658890800,\"main\":{\"temp\":295.46,\"feels_like\":295.86,\"temp_min\":295.46,\"temp_max\":295.46,\"pressure\":1010,\"sea_level\":1010,\"grnd_level\":1008,\"humidity\":81,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.24,\"deg\":162,\"gust\":4.38},\"visibility\":10000,\"pop\":0.07,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-27 03:00:00\"}," +
            "{\"dt\":1658901600,\"main\":{\"temp\":298.76,\"feels_like\":299,\"temp_min\":298.76,\"temp_max\":298.76,\"pressure\":1011,\"sea_level\":1011,\"grnd_level\":1009,\"humidity\":62,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.21,\"deg\":187,\"gust\":4.66},\"visibility\":10000,\"pop\":0.01,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-07-27 06:00:00\"}]," +
            "\"city\":{\"id\":582182,\"name\":\"Анапа\",\"coord\":{\"lat\":44.8908,\"lon\":37.3239},\"country\":\"RU\",\"population\":53600,\"timezone\":10800,\"sunrise\":1658455502,\"sunset\":1658509733}}";

    @Test
    public void testApproachToParseJsonArray() throws JsonProcessingException {
        List<String> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDate.now().atTime(00,00,00);
        LocalDateTime dateTime1 = LocalDate.now().atTime(06,00,00);
        LocalDateTime dateTime2 = LocalDate.now().atTime(12,00,00);

        for (long i = 1; i<=3; i++) {
            result.add(dateTime.plusDays(i).format(formatter));
            result.add(dateTime1.plusDays(i).format(formatter));
            result.add(dateTime2.plusDays(i).format(formatter));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode list = (ArrayNode) objectMapper.readTree(weatherResponse).get("list");

        ArrayList<Integer> arrayForIndexes = new ArrayList<>();
        Iterator<JsonNode> jsonArrayIterator = list.iterator();
        int tempCounter = 0;

        while (jsonArrayIterator.hasNext()) {
            String jsonNodeDttxt = jsonArrayIterator.next().path("dt_txt").asText();
            if (result.contains(jsonNodeDttxt)) {
                arrayForIndexes.add(tempCounter);
            }
            tempCounter++;
        }

        assertArrayEquals(new Integer[]{13, 15, 17, 21, 23, 25, 29, 31, 33}, arrayForIndexes.toArray(new Integer[0]));
    }
}