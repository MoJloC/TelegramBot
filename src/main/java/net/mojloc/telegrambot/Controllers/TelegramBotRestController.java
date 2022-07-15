package net.mojloc.telegrambot.Controllers;

import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.TelegramBot;
import net.mojloc.telegrambot.services.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
/*
At @RequestMapping used "/callback" instead of "/rest/v1" because of strange (freaky) realisation of API method
 getBotUrl(String externalUrl, String botPath) at public final class WebhookUtil
 */
@RequestMapping("/callback/rest/v1")
@Slf4j
public class TelegramBotRestController {
    private final TelegramBot telegramBot;

    @Autowired
    public TelegramBotRestController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/ConnectionTest")
    @ResponseStatus(HttpStatus.OK)
    public void connectionTest(RequestEntity<String> request) {
        log.info("********************** Parameters of Request ********************** ");
        log.info("Url: " + request.getUrl());
        log.info("Body: " + request.getBody());
        log.info("Headers: " + request.getHeaders());
        log.info("Method: " + request.getMethod());
        log.info("************************ End of parameters ************************ ");
    }

    @PostMapping("/")
    public BotApiMethod onUpdateReceived(@RequestBody Update update, RequestEntity<String> request) {
        log.info("********************** Parameters of Request ********************** ");
        log.info("Url: " + request.getUrl());
        log.info("Body: " + request.getBody());
        log.info("Headers: " + request.getHeaders());
        log.info("Method: " + request.getMethod());
        log.info("************************ End of parameters ************************ ");
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
