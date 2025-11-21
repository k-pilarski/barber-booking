package com.example.barberbooking.controller;

import com.example.barberbooking.entity.ExternalNotification;
import com.example.barberbooking.repository.ExternalNotificationRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all external notifications", description = "Returns a list of all external notifications in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<ExternalNotification>> getAllExternalNotifications() {
        List<ExternalNotification> list = externalNotificationRepo.findAll();
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
        Optional<ExternalNotification> item = externalNotificationRepo.findById(id);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new external notification", description = "Adds a new external notification to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "External notification successfully created")
    })
    @PostMapping
    public ResponseEntity<ExternalNotification> create(
            @Parameter(description = "ExternalNotification object to be created") @RequestBody ExternalNotification notification) {
        ExternalNotification saved = externalNotificationRepo.save(notification);
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
            @Parameter(description = "Updated external notification details") @RequestBody ExternalNotification details) {
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

    @Operation(summary = "Delete an external notification", description = "Deletes an external notification by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "External notification successfully deleted"),
        @ApiResponse(responseCode = "404", description = "External notification not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(
            @Parameter(description = "ID of the external notification to delete") @PathVariable Long id) {
        if (externalNotificationRepo.existsById(id)) {
            externalNotificationRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
