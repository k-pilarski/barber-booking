package com.example.barberbooking.controller;

import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.repository.BarberRepo;
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

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/barbers")
public class BarberController {

    private static final Logger logger = LogManager.getLogger(BarberController.class);

    @Autowired
    private BarberRepo barberRepo;

    @Operation(summary = "Get all barbers", description = "Returns a list of all barbers in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers() {
        logger.info("Wywołanie metody getAllBarbers");
        List<Barber> barbers = barberRepo.findAll();
        logger.debug("Liczba barberów: {}", barbers.size());
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
        logger.info("Wywołanie getBarberById dla id: {}", id);
        Optional<Barber> barber = barberRepo.findById(id);
        if (barber.isPresent()) {
            return new ResponseEntity<>(barber.get(), HttpStatus.OK);
        } else {
            logger.error("Barber o id {} nie znaleziony", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new barber", description = "Adds a new barber to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Barber successfully created")
    })
    @PostMapping
    public ResponseEntity<Barber> createBarber(
            @Parameter(description = "Barber object to be created") @RequestBody Barber barber) {
        logger.info("Tworzenie nowego barbera: {}", barber.getName());
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
        logger.info("Aktualizacja barbera o id: {}", id);
        logger.debug("Dane do aktualizacji: {}", barberDetails);

        Optional<Barber> optionalBarber = barberRepo.findById(id);
        if (optionalBarber.isPresent()) {
            Barber barber = optionalBarber.get();
            barber.setName(barberDetails.getName());
            barber.setPhone(barberDetails.getPhone());
            barber.setSpecialty(barberDetails.getSpecialty());
            Barber updatedBarber = barberRepo.save(barber);
            return new ResponseEntity<>(updatedBarber, HttpStatus.OK);
        } else {
            logger.error("Nie znaleziono barbera o id: {}", id);
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
        logger.info("Usuwanie barbera o id: {}", id);
        if (barberRepo.existsById(id)) {
            barberRepo.deleteById(id);
            logger.info("Barber o id {} został usunięty", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Nie znaleziono barbera do usunięcia o id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

