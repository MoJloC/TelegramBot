package net.mojloc.telegrambot.controllers;

import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.model.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@RestController
/*
At @RequestMapping used "/callback" instead of "/rest/v1" because of strange realisation of API method
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
    public void connectionTest() {}

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update, RequestEntity<String> request) {

        log.info("Update ID " + update.getUpdateId()
                + ": received. Content-Type " + request.getHeaders().getContentType()
                + ". HTTP method " + request.getMethod()
                + ". URL: " + request.getUrl());

        if (!telegramBot.getSetWebhook().getSecretToken().equals(Objects.requireNonNull(request.getHeaders()
                                                                 .get("x-telegram-bot-api-secret-token")).get(0))) {
            log.error("Update ID " + update.getUpdateId() + ": Warning!!! Secret key at Update object incorrect:\n"
                      + Objects.requireNonNull(request.getHeaders().get("x-telegram-bot-api-secret-token")).get(0)
                      + " instead of " + telegramBot.getSetWebhook().getSecretToken()
                      + "\n Update object rejected!");
            return null;
        }

        return telegramBot.onWebhookUpdateReceived(update);
    }
}
