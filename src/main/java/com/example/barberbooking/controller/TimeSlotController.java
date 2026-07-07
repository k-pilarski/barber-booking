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

import com.example.barberbooking.dto.TimeSlotDto;
import com.example.barberbooking.service.TimeSlotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@CrossOrigin
@RestController
@RequestMapping("/timeslots")
@Log4j2
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @Operation(summary = "Get all time slots", description = "Returns a list of all time slots in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<TimeSlotDto>> getAllTimeSlots() {
        log.info("Żądanie: getAllTimeSlots()");
        List<TimeSlotDto> slots = timeSlotService.getAllTimeSlots();
        log.debug("Pobrano {} przedziałów czasowych", slots.size());
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }

    @Operation(summary = "Get time slot by ID", description = "Returns a single time slot by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time slot found"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TimeSlotDto> getTimeSlotById(
            @Parameter(description = "ID of the time slot to retrieve") @PathVariable Long id) {

        log.info("Żądanie: getTimeSlotById({})", id);
        Optional<TimeSlotDto> slot = timeSlotService.getTimeSlotById(id);

        if (slot.isPresent()) {
            log.debug("Znaleziono przedział czasowy {}", id);
            return new ResponseEntity<>(slot.get(), HttpStatus.OK);
        } else {
            log.warn("NIE znaleziono przedziału czasowego {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new time slot", description = "Adds a new time slot to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Time slot successfully created")
    })
    @PostMapping
    public ResponseEntity<TimeSlotDto> createTimeSlot(
            @Parameter(description = "Time slot object to be created") @RequestBody TimeSlotDto slotDto) {

        log.info("Żądanie: createTimeSlot()");
        TimeSlotDto savedSlot = timeSlotService.createTimeSlot(slotDto);
        log.info("Utworzono przedział czasowy o ID {}", savedSlot.getId());

        return new ResponseEntity<>(savedSlot, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing time slot", description = "Updates details of an existing time slot by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time slot successfully updated"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TimeSlotDto> updateTimeSlot(
            @Parameter(description = "ID of the time slot to update") @PathVariable Long id,
            @Parameter(description = "Updated time slot details") @RequestBody TimeSlotDto slotDetails) {

        log.info("Żądanie: updateTimeSlot({})", id);

        Optional<TimeSlotDto> updatedSlot = timeSlotService.updateTimeSlot(id, slotDetails);
        if (updatedSlot.isPresent()) {
            log.info("Przedział czasowy {} pomyślnie zaktualizowany", id);
            return new ResponseEntity<>(updatedSlot.get(), HttpStatus.OK);
        } else {
            log.warn("NIE znaleziono przedziału czasowego {} – nie można zaktualizować", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a time slot", description = "Deletes a time slot by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Time slot successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTimeSlot(
            @Parameter(description = "ID of the time slot to delete") @PathVariable Long id) {

        log.info("Żądanie: deleteTimeSlot({})", id);

        if (timeSlotService.deleteTimeSlot(id)) {
            log.info("Usunięto przedział czasowy {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("NIE znaleziono przedziału czasowego {} – nie można usunąć", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
