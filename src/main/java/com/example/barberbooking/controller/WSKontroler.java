package com.example.barberbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class WSKontroler {

    private final SimpMessagingTemplate template;

    @Autowired
    public WSKontroler(SimpMessagingTemplate template) {
        this.template = template;
    }

    // Endpoint wywoływany np. przy dodaniu nowego użytkownika
    @PostMapping("/news/{userId}")
    public void publikuj(@PathVariable("userId") int userId) {
        String message = "Ważna wiadomość dla użytkownika " + userId;
        this.template.convertAndSend("/topic/news" + userId, message);
    }
}