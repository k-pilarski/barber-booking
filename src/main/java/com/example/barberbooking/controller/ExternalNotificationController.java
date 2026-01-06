package com.example.barberbooking.controller;

import java.util.List;
import java.util.Optional;

import com.example.barberbooking.entity.ExternalNotification;
import com.example.barberbooking.repository.ExternalNotificationRepo;

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
@RequestMapping("/external-notifications")
public class ExternalNotificationController {

    private static final Logger logger = LogManager.getLogger(ExternalNotificationController.class);

    @Autowired
    private ExternalNotificationRepo externalNotificationRepo;

    @Operation(summary = "Get all external notifications", description = "Returns a list of all external notifications in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<ExternalNotification>> getAllExternalNotifications() {
        logger.info("Wywołanie metody getAllExternalNotifications");
        List<ExternalNotification> list = externalNotificationRepo.findAll();
        logger.debug("Liczba zewnętrznych notyfikacji: {}", list.size());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Get external notification by ID", description = "Returns a single external notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "External notification found"),
            @ApiResponse(responseCode = "404", description = "External notification not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExternalNotification> getById(
            @Parameter(description = "ID of the external notification to retrieve") @PathVariable Long id) {

        logger.info("Wywołanie getById dla externalNotification id: {}", id);

        Optional<ExternalNotification> item = externalNotificationRepo.findById(id);

        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            logger.error("Zewnętrzna notyfikacja o id {} nie została znaleziona", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new external notification", description = "Adds a new external notification to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "External notification successfully created")
    })
    @PostMapping
    public ResponseEntity<ExternalNotification> create(
            @Parameter(description = "ExternalNotification object to be created")
            @RequestBody ExternalNotification notification) {

        logger.info("Tworzenie nowej zewnętrznej notyfikacji");

        ExternalNotification saved = externalNotificationRepo.save(notification);

        logger.info("Utworzono zewnętrzną notyfikację o id: {}", saved.getId());

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing external notification", description = "Updates details of an existing external notification by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "External notification successfully updated"),
            @ApiResponse(responseCode = "404", description = "External notification not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExternalNotification> update(
            @Parameter(description = "ID of the external notification to update") @PathVariable Long id,
            @Parameter(description = "Updated external notification details")
            @RequestBody ExternalNotification details) {

        logger.info("Aktualizacja zewnętrznej notyfikacji o id: {}", id);

        Optional<ExternalNotification> optional = externalNotificationRepo.findById(id);

        if (optional.isPresent()) {
            ExternalNotification notification = optional.get();

            notification.setAppointment(details.getAppointment());
            notification.setTargetService(details.getTargetService());
            notification.setPayload(details.getPayload());
            notification.setStatus(details.getStatus());
            notification.setCreatedAt(details.getCreatedAt());

            ExternalNotification updated = externalNotificationRepo.save(notification);

            logger.info("Zewnętrzna notyfikacja o id {} została zaktualizowana", id);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            logger.error("Nie znaleziono zewnętrznej notyfikacji o id {} do aktualizacji", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an external notification", description = "Deletes an external notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "External notification successfully deleted"),
            @ApiResponse(responseCode = "404", description = "External notification not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(
            @Parameter(description = "ID of the external notification to delete") @PathVariable Long id) {

        logger.info("Usuwanie zewnętrznej notyfikacji o id: {}", id);

        if (externalNotificationRepo.existsById(id)) {
            externalNotificationRepo.deleteById(id);
            logger.info("Zewnętrzna notyfikacja o id {} została usunięta", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Nie znaleziono zewnętrznej notyfikacji o id {} do usunięcia", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
