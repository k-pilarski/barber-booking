package com.example.barberbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class WSKontroler {

    private final SimpMessagingTemplate template;

    @Autowired
    public WSKontroler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping("/news/{userId}")
    public void publikuj(@PathVariable("userId") int userId) {
        String message = "Ważna wiadomość dla użytkownika " + userId;
        try {
            this.template.convertAndSend("/topic/news" + userId, message);
            log.info("Wysłano powiadomienie WebSocket do użytkownika {}: {}", userId, message);
        } catch (Exception e) {
            log.error("Błąd podczas wysyłania powiadomienia WebSocket do użytkownika {}", userId, e);
        }
    }
}