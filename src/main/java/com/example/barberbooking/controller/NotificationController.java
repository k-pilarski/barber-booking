package com.example.barberbooking.controller;

import com.example.barberbooking.entity.NotificationLog;
import com.example.barberbooking.repository.NotificationLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationLogRepo notificationLogRepo;

    @GetMapping
    public ResponseEntity<List<NotificationLog>> getAllNotifications() {
        List<NotificationLog> notifications = notificationLogRepo.findAll();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationLog> getNotificationById(@PathVariable Long id) {
        Optional<NotificationLog> notification = notificationLogRepo.findById(id);
        return notification.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                           .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<NotificationLog> createNotification(@RequestBody NotificationLog notification) {
        NotificationLog savedNotification = notificationLogRepo.save(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationLog> updateNotification(@PathVariable Long id, @RequestBody NotificationLog details) {
        Optional<NotificationLog> optionalNotification = notificationLogRepo.findById(id);
        if (optionalNotification.isPresent()) {
            NotificationLog notification = optionalNotification.get();
            notification.setAppointment(details.getAppointment());
            notification.setType(details.getType());
            notification.setPayload(details.getPayload());
            notification.setSentAt(details.getSentAt());
            notification.setStatus(details.getStatus());
            NotificationLog updated = notificationLogRepo.save(notification);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNotification(@PathVariable Long id) {
        if (notificationLogRepo.existsById(id)) {
            notificationLogRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
