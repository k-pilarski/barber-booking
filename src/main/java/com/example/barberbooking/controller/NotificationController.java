package com.example.barberbooking.controller;

import java.util.List;
import java.util.Optional;

import com.example.barberbooking.entity.NotificationLog;
import com.example.barberbooking.repository.NotificationLogRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LogManager.getLogger(NotificationController.class);

    @Autowired
    private NotificationLogRepo notificationLogRepo;

    @Operation(summary = "Get all notifications", description = "Returns a list of all notifications in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<NotificationLog>> getAllNotifications() {
        logger.info("Wywołanie metody getAllNotifications");
        List<NotificationLog> notifications = notificationLogRepo.findAll();
        logger.debug("Liczba notyfikacji: {}", notifications.size());
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @Operation(summary = "Get notification by ID", description = "Returns a single notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification found"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationLog> getNotificationById(
            @Parameter(description = "ID of the notification to retrieve")
            @PathVariable Long id) {

        logger.info("Wywołanie getNotificationById dla id: {}", id);

        Optional<NotificationLog> notification = notificationLogRepo.findById(id);

        if (notification.isPresent()) {
            return new ResponseEntity<>(notification.get(), HttpStatus.OK);
        } else {
            logger.error("Notyfikacja o id {} nie została znaleziona", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new notification", description = "Adds a new notification to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification successfully created")
    })
    @PostMapping
    public ResponseEntity<NotificationLog> createNotification(
            @Parameter(description = "Notification object to be created")
            @RequestBody NotificationLog notification) {

        logger.info("Tworzenie nowej notyfikacji");

        NotificationLog savedNotification = notificationLogRepo.save(notification);

        logger.info("Utworzono notyfikację o id: {}", savedNotification.getId());

        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing notification", description = "Updates details of an existing notification by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification successfully updated"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificationLog> updateNotification(
            @Parameter(description = "ID of the notification to update")
            @PathVariable Long id,
            @Parameter(description = "Updated notification details")
            @RequestBody NotificationLog details) {

        logger.info("Aktualizacja notyfikacji o id: {}", id);

        Optional<NotificationLog> optionalNotification = notificationLogRepo.findById(id);

        if (optionalNotification.isPresent()) {
            NotificationLog notification = optionalNotification.get();

            notification.setAppointment(details.getAppointment());
            notification.setType(details.getType());
            notification.setPayload(details.getPayload());
            notification.setSentAt(details.getSentAt());
            notification.setStatus(details.getStatus());

            NotificationLog updated = notificationLogRepo.save(notification);

            logger.info("Notyfikacja o id {} została zaktualizowana", id);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            logger.error("Nie znaleziono notyfikacji o id {} do aktualizacji", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a notification", description = "Deletes a notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNotification(
            @Parameter(description = "ID of the notification to delete")
            @PathVariable Long id) {

        logger.info("Usuwanie notyfikacji o id: {}", id);

        if (notificationLogRepo.existsById(id)) {
            notificationLogRepo.deleteById(id);
            logger.info("Notyfikacja o id {} została usunięta", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Nie znaleziono notyfikacji o id {} do usunięcia", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
