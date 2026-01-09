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

import com.example.barberbooking.entity.Appointment;
import com.example.barberbooking.repository.AppointmentRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Operation(summary = "Get all appointments", description = "Returns a list of all appointments in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        logger.info("Method call getAllAppointments");
        List<Appointment> appointments = appointmentRepo.findAll();
        logger.debug("Number of visits: {}", appointments.size());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Operation(summary = "Get appointment by ID", description = "Returns a single appointment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @Parameter(description = "ID of the appointment to retrieve") @PathVariable Long id) {

        logger.info("Calling getAppointmentById for id: {}", id);

        Optional<Appointment> appointment = appointmentRepo.findById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            logger.error("The visit with id {} was not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new appointment", description = "Adds a new appointment to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment successfully created")
    })
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @Parameter(description = "Appointment object to be created") @RequestBody Appointment appointment) {

        logger.info("Creating a new visit");
        Appointment savedAppointment = appointmentRepo.save(appointment);
        logger.info("A visit with id was created: {}", savedAppointment.getId());

        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing appointment", description = "Updates details of an existing appointment by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully updated"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @Parameter(description = "ID of the appointment to update") @PathVariable Long id,
            @Parameter(description = "Updated appointment details") @RequestBody Appointment appointmentDetails) {

        logger.info("Update visit with id: {}", id);

        Optional<Appointment> optionalAppointment = appointmentRepo.findById(id);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            appointment.setBarber(appointmentDetails.getBarber());
            appointment.setStartTime(appointmentDetails.getStartTime());
            appointment.setEndTime(appointmentDetails.getEndTime());
            appointment.setClientName(appointmentDetails.getClientName());
            appointment.setClientPhone(appointmentDetails.getClientPhone());
            appointment.setStatus(appointmentDetails.getStatus());

            Appointment updatedAppointment = appointmentRepo.save(appointment);

            logger.info("The visit with id {} has been updated", id);

            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } else {
            logger.error("No visit with id {} found for update", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an appointment", description = "Deletes an appointment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete") @PathVariable Long id) {

        logger.info("Deleting visit with id: {}", id);

        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            logger.info("The visit with id {} has been deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("No visit found with id {} to be deleted", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
