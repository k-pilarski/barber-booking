package com.example.barberbooking.controller;

import com.example.barberbooking.entity.TimeSlot;
import com.example.barberbooking.repository.TimeSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        List<TimeSlot> slots = timeSlotRepo.findAll();
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(@PathVariable Long id) {
        Optional<TimeSlot> slot = timeSlotRepo.findById(id);
        return slot.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlot slot) {
        TimeSlot savedSlot = timeSlotRepo.save(slot);
        return new ResponseEntity<>(savedSlot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(@PathVariable Long id, @RequestBody TimeSlot slotDetails) {
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

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTimeSlot(@PathVariable Long id) {
        if (timeSlotRepo.existsById(id)) {
            timeSlotRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
