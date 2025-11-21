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

@CrossOrigin
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Operation(summary = "Get all time slots", description = "Returns a list of all time slots in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        List<TimeSlot> slots = timeSlotRepo.findAll();
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
        Optional<TimeSlot> slot = timeSlotRepo.findById(id);
        return slot.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new time slot", description = "Adds a new time slot to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Time slot successfully created")
    })
    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(
            @Parameter(description = "Time slot object to be created") @RequestBody TimeSlot slot) {
        TimeSlot savedSlot = timeSlotRepo.save(slot);
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
        Optional<TimeSlot> optionalSlot = timeSlotRepo.findById(id);
        if (optionalSlot.isPresent()) {
            TimeSlot slot = optionalSlot.get();
            slot.setBarber(slotDetails.getBarber());
            slot.setStartTime(slotDetails.getStartTime());
            slot.setEndTime(slotDetails.getEndTime());
            slot.setIsAvailable(slotDetails.getIsAvailable());
            TimeSlot updatedSlot = timeSlotRepo.save(slot);
            return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
        } else {
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
        if (timeSlotRepo.existsById(id)) {
            timeSlotRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
