package com.example.barberbooking.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barberbooking.entity.NotificationLog;
import com.example.barberbooking.repository.NotificationLogRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

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
        logger.info("Calling the getAllNotifications method");
        List<NotificationLog> notifications = notificationLogRepo.findAll();
        logger.debug("Number of notifications: {}", notifications.size());
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

        logger.info("Calling getNotificationById for id: {}", id);

        Optional<NotificationLog> notification = notificationLogRepo.findById(id);

        if (notification.isPresent()) {
            return new ResponseEntity<>(notification.get(), HttpStatus.OK);
        } else {
            logger.error("Notification with id {} not found", id);
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

        logger.info("Creating a new notification");

        NotificationLog savedNotification = notificationLogRepo.save(notification);

        logger.info("Notification with id created: {}", savedNotification.getId());

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

        logger.info("Notification update with id: {}", id);

        Optional<NotificationLog> optionalNotification = notificationLogRepo.findById(id);

        if (optionalNotification.isPresent()) {
            NotificationLog notification = optionalNotification.get();

            notification.setAppointment(details.getAppointment());
            notification.setType(details.getType());
            notification.setPayload(details.getPayload());
            notification.setSentAt(details.getSentAt());
            notification.setStatus(details.getStatus());

            NotificationLog updated = notificationLogRepo.save(notification);

            logger.info("The notification with id {} has been updated", id);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            logger.error("No notification with id {} found for update", id);
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

        logger.info("Deleting notifications with ID: {}", id);

        if (notificationLogRepo.existsById(id)) {
            notificationLogRepo.deleteById(id);
            logger.info("The notification with id {} has been deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("No notification with id {} found to be deleted", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
