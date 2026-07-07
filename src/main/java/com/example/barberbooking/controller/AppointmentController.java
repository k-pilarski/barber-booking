package com.example.barberbooking.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
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

import com.example.barberbooking.dto.AppointmentCreateRequest;
import com.example.barberbooking.dto.AppointmentDto;
import com.example.barberbooking.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Operation(summary = "Get all appointments", description = "Returns a list of all appointments in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        logger.info("Wywołanie metody getAllAppointments");
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        logger.debug("Liczba wizyt: {}", appointments.size());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Operation(summary = "Get appointment by ID", description = "Returns a single appointment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(
            @Parameter(description = "ID of the appointment to retrieve") @PathVariable Long id) {

        logger.info("Wywołanie getAppointmentById dla id: {}", id);
        Optional<AppointmentDto> appointment = appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            logger.error("Wizyta o id {} nie została znaleziona", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new appointment", description = "Adds a new appointment to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment successfully created")
    })
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(
            @Parameter(description = "Appointment object to be created") @Valid @RequestBody AppointmentCreateRequest request) {

        logger.info("Tworzenie nowej wizyty");
        AppointmentDto savedAppointment = appointmentService.createAppointment(request);
        logger.info("Utworzono wizytę o id: {}", savedAppointment.getId());

        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing appointment", description = "Updates details of an existing appointment by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully updated"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @Parameter(description = "ID of the appointment to update") @PathVariable Long id,
            @Parameter(description = "Updated appointment details") @Valid @RequestBody AppointmentCreateRequest request) {

        logger.info("Aktualizacja wizyty o id: {}", id);

        Optional<AppointmentDto> updatedAppointment = appointmentService.updateAppointment(id, request);

        if (updatedAppointment.isPresent()) {
            logger.info("Wizyta o id {} została zaktualizowana", id);
            return new ResponseEntity<>(updatedAppointment.get(), HttpStatus.OK);
        } else {
            logger.error("Nie znaleziono wizyty o id {} do aktualizacji", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an appointment", description = "Deletes an appointment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete") @PathVariable Long id) {

        logger.info("Usuwanie wizyty o id: {}", id);

        if (appointmentService.deleteAppointment(id)) {
            logger.info("Wizyta o id {} została usunięta", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Nie znaleziono wizyty o id {} do usunięcia", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
