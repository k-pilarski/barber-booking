package com.example.barberbooking.controller;

import com.example.barberbooking.entity.ExternalNotification;
import com.example.barberbooking.repository.ExternalNotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/external-notifications")
public class ExternalNotificationController {

    @Autowired
    private ExternalNotificationRepo externalNotificationRepo;

    @GetMapping
    public ResponseEntity<List<ExternalNotification>> getAllExternalNotifications() {
        List<ExternalNotification> list = externalNotificationRepo.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalNotification> getById(@PathVariable Long id) {
        Optional<ExternalNotification> item = externalNotificationRepo.findById(id);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ExternalNotification> create(@RequestBody ExternalNotification notification) {
        ExternalNotification saved = externalNotificationRepo.save(notification);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExternalNotification> update(@PathVariable Long id, @RequestBody ExternalNotification details) {
        Optional<ExternalNotification> optional = externalNotificationRepo.findById(id);
        if (optional.isPresent()) {
            ExternalNotification notification = optional.get();
            notification.setAppointment(details.getAppointment());
            notification.setTargetService(details.getTargetService());
            notification.setPayload(details.getPayload());
            notification.setStatus(details.getStatus());
            notification.setCreatedAt(details.getCreatedAt());
            ExternalNotification updated = externalNotificationRepo.save(notification);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        if (externalNotificationRepo.existsById(id)) {
            externalNotificationRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
