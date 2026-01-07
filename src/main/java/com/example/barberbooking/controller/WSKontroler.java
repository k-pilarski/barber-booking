package com.example.barberbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class WSKontroler {

    private final SimpMessagingTemplate template;

    @Autowired
    public WSKontroler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping("/news/{userId}")
    public void publikuj(@PathVariable("userId") int userId) {
        String message = "Important message for the user " + userId;
        this.template.convertAndSend("/topic/news" + userId, message);
    }
}