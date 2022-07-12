package net.mojloc.telegrambot.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1")
@Slf4j
public class TelegramBotRestController {

    @PostMapping("/ConnectionTest")
    @ResponseStatus(HttpStatus.OK)
    public void connectionTest() {

    }
}
