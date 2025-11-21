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

import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.repository.BarberRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/barbers")
public class BarberController {

    @Autowired
    private BarberRepo barberRepo;

    @Operation(summary = "Get all barbers", description = "Returns a list of all barbers in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers() {
        List<Barber> barbers = barberRepo.findAll();
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }

    @Operation(summary = "Get barber by ID", description = "Returns a single barber by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barber found"),
        @ApiResponse(responseCode = "404", description = "Barber not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(
            @Parameter(description = "ID of the barber to retrieve") @PathVariable Long id) {
        Optional<Barber> barber = barberRepo.findById(id);
        return barber.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new barber", description = "Adds a new barber to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Barber successfully created")
    })
    @PostMapping
    public ResponseEntity<Barber> createBarber(
            @Parameter(description = "Barber object to be created") @RequestBody Barber barber) {
        Barber savedBarber = barberRepo.save(barber);
        return new ResponseEntity<>(savedBarber, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing barber", description = "Updates details of an existing barber by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barber successfully updated"),
        @ApiResponse(responseCode = "404", description = "Barber not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(
            @Parameter(description = "ID of the barber to update") @PathVariable Long id,
            @Parameter(description = "Updated barber details") @RequestBody Barber barberDetails) {
        Optional<Barber> optionalBarber = barberRepo.findById(id);
        if (optionalBarber.isPresent()) {
            Barber barber = optionalBarber.get();
            barber.setName(barberDetails.getName());
            barber.setPhone(barberDetails.getPhone());
            barber.setSpecialty(barberDetails.getSpecialty());
            Barber updatedBarber = barberRepo.save(barber);
            return new ResponseEntity<>(updatedBarber, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a barber", description = "Deletes a barber by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Barber successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Barber not found")
    })
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBarber(
            @Parameter(description = "ID of the barber to delete") @PathVariable Long id) {
        if (barberRepo.existsById(id)) {
            barberRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
