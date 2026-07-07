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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barberbooking.dto.BarberDto;
import com.example.barberbooking.service.BarberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/barbers")
public class BarberController {

    private static final Logger logger = LogManager.getLogger(BarberController.class);

    @Autowired
    private BarberService barberService;

    @Operation(summary = "Get all barbers", description = "Returns a list of all barbers in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<BarberDto>> getAllBarbers() {
        logger.info("Wywołanie metody getAllBarbers");
        List<BarberDto> barbers = barberService.getAllBarbers();
        logger.debug("Liczba barberów: {}", barbers.size());
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }

    @Operation(summary = "Get barber by ID", description = "Returns a single barber by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Barber found"),
            @ApiResponse(responseCode = "404", description = "Barber not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BarberDto> getBarberById(
            @Parameter(description = "ID of the barber to retrieve") @PathVariable Long id) {
        logger.info("Wywołanie getBarberById dla id: {}", id);
        Optional<BarberDto> barber = barberService.getBarberById(id);
        if (barber.isPresent()) {
            return new ResponseEntity<>(barber.get(), HttpStatus.OK);
        } else {
            logger.error("Barber o id {} nie został znaleziony", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new barber", description = "Adds a new barber to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Barber successfully created")
    })
    @PostMapping
    public ResponseEntity<BarberDto> createBarber(
            @Parameter(description = "Barber object to be created") @RequestBody BarberDto barberDto) {
        logger.info("Tworzenie nowego barbera: {}", barberDto.getName());
        BarberDto savedBarber = barberService.createBarber(barberDto);
        return new ResponseEntity<>(savedBarber, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing barber", description = "Updates details of an existing barber by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Barber successfully updated"),
            @ApiResponse(responseCode = "404", description = "Barber not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BarberDto> updateBarber(
            @Parameter(description = "ID of the barber to update") @PathVariable Long id,
            @Parameter(description = "Updated barber details") @RequestBody BarberDto barberDetails) {
        logger.info("Aktualizacja barbera o id: {}", id);

        Optional<BarberDto> updatedBarber = barberService.updateBarber(id, barberDetails);
        if (updatedBarber.isPresent()) {
            return new ResponseEntity<>(updatedBarber.get(), HttpStatus.OK);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBarber(
            @Parameter(description = "ID of the barber to delete") @PathVariable Long id) {
        logger.info("Usuwanie barbera o id: {}", id);
        if (barberService.deleteBarber(id)) {
            logger.info("Barber o id {} został usunięty", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Nie znaleziono barbera do usunięcia o id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search barbers by specialty (Sorted)", description = "Uses JPQL to fetch barbers by specialty, sorted by name.")
    @GetMapping("/search/specialty")
    public ResponseEntity<List<BarberDto>> getBarbersBySpecialtySorted(@RequestParam String specialty) {
        logger.info("Wyszukiwanie barberów po specjalności: {}", specialty);
        List<BarberDto> barbers = barberService.getBarbersBySpecialtySorted(specialty);
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }

    @Operation(summary = "Search barbers by name fragment (Native SQL)", description = "Uses Native SQL query to find barbers containing the name fragment.")
    @GetMapping("/search/name")
    public ResponseEntity<List<BarberDto>> searchBarbersByNameNative(@RequestParam String nameFragment) {
        logger.info("Wyszukiwanie barberów po fragmencie nazwy: {}", nameFragment);
        List<BarberDto> barbers = barberService.searchBarbersByNameNative(nameFragment);
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }
}
