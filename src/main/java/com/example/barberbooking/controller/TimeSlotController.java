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

import com.example.barberbooking.entity.TimeSlot;
import com.example.barberbooking.repository.TimeSlotRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@CrossOrigin
@RestController
@RequestMapping("/timeslots")
@Log4j2
public class TimeSlotController {

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Operation(summary = "Get all time slots", description = "Returns a list of all time slots in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        log.info("Request: getAllTimeSlots()");
        List<TimeSlot> slots = timeSlotRepo.findAll();
        log.debug("Retrieved {} time slots", slots.size());
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }

    @Operation(summary = "Get time slot by ID", description = "Returns a single time slot by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time slot found"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(
            @Parameter(description = "ID of the time slot to retrieve") @PathVariable Long id) {

        log.info("Request: getTimeSlotById({})", id);
        Optional<TimeSlot> slot = timeSlotRepo.findById(id);

        if (slot.isPresent()) {
            log.debug("Time slot {} found", id);
            return new ResponseEntity<>(slot.get(), HttpStatus.OK);
        } else {
            log.warn("Time slot {} NOT found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new time slot", description = "Adds a new time slot to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Time slot successfully created")
    })
    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(
            @Parameter(description = "Time slot object to be created") @RequestBody TimeSlot slot) {

        log.info("Request: createTimeSlot()");
        log.debug("TimeSlot data: {}", slot);

        TimeSlot savedSlot = timeSlotRepo.save(slot);
        log.info("Time slot created with ID {}", savedSlot.getId());

        return new ResponseEntity<>(savedSlot, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing time slot", description = "Updates details of an existing time slot by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time slot successfully updated"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(
            @Parameter(description = "ID of the time slot to update") @PathVariable Long id,
            @Parameter(description = "Updated time slot details") @RequestBody TimeSlot slotDetails) {

        log.info("Request: updateTimeSlot({})", id);
        log.debug("Update data: {}", slotDetails);

        Optional<TimeSlot> optionalSlot = timeSlotRepo.findById(id);
        if (optionalSlot.isPresent()) {
            TimeSlot slot = optionalSlot.get();
            slot.setBarber(slotDetails.getBarber());
            slot.setStartTime(slotDetails.getStartTime());
            slot.setEndTime(slotDetails.getEndTime());
            slot.setIsAvailable(slotDetails.getIsAvailable());

            TimeSlot updatedSlot = timeSlotRepo.save(slot);
            log.info("Time slot {} updated successfully", id);

            return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
        } else {
            log.warn("Time slot {} NOT found – cannot update", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a time slot", description = "Deletes a time slot by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Time slot successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTimeSlot(
            @Parameter(description = "ID of the time slot to delete") @PathVariable Long id) {

        log.info("Request: deleteTimeSlot({})", id);

        if (timeSlotRepo.existsById(id)) {
            timeSlotRepo.deleteById(id);
            log.warn("Time slot {} deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("Time slot {} NOT found – cannot delete", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
