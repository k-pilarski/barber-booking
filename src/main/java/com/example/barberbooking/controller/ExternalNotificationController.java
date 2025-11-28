package com.example.barberbooking.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.barberbooking.entity.ExternalNotification;
import com.example.barberbooking.repository.ExternalNotificationRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@CrossOrigin
@RestController
@RequestMapping("/external-notifications")
@Log4j2
public class ExternalNotificationController {

    @Autowired
    private ExternalNotificationRepo externalNotificationRepo;

    @Operation(summary = "Get all external notifications", description = "Returns a list of all external notifications in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<ExternalNotification>> getAllExternalNotifications() {
        log.info("Request: getAllExternalNotifications()");
        List<ExternalNotification> list = externalNotificationRepo.findAll();
        log.debug("Retrieved {} external notifications", list.size());
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

        log.info("Request: getById({})", id);
        Optional<ExternalNotification> item = externalNotificationRepo.findById(id);

        if (item.isPresent()) {
            log.debug("External notification {} found", id);
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            log.warn("External notification {} NOT found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new external notification", description = "Adds a new external notification to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "External notification successfully created")
    })
    @PostMapping
    public ResponseEntity<ExternalNotification> create(
            @Parameter(description = "ExternalNotification object to be created") @RequestBody ExternalNotification notification) {

        log.info("Request: create() new external notification");
        log.debug("ExternalNotification data: {}", notification);

        ExternalNotification saved = externalNotificationRepo.save(notification);

        log.info("External notification created with ID {}", saved.getId());

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

        log.info("Request: update({})", id);
        log.debug("Update data: {}", details);

        Optional<ExternalNotification> optional = externalNotificationRepo.findById(id);

        if (optional.isPresent()) {
            ExternalNotification notification = optional.get();
            notification.setAppointment(details.getAppointment());
            notification.setTargetService(details.getTargetService());
            notification.setPayload(details.getPayload());
            notification.setStatus(details.getStatus());
            notification.setCreatedAt(details.getCreatedAt());

            ExternalNotification updated = externalNotificationRepo.save(notification);

            log.info("External notification {} updated successfully", id);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            log.warn("External notification {} NOT found – cannot update", id);
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

        log.info("Request: delete({})", id);

        if (externalNotificationRepo.existsById(id)) {
            externalNotificationRepo.deleteById(id);
            log.warn("External notification {} deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("External notification {} NOT found – cannot delete", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}