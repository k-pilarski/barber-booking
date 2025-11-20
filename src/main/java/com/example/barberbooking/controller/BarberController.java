package com.example.barberbooking.controller;

import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.repository.BarberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/barbers")
public class BarberController {

    @Autowired
    private BarberRepo barberRepo;

    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers() {
        List<Barber> barbers = barberRepo.findAll();
        return new ResponseEntity<>(barbers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(@PathVariable Long id) {
        Optional<Barber> barber = barberRepo.findById(id);
        return barber.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Barber> createBarber(@RequestBody Barber barber) {
        Barber savedBarber = barberRepo.save(barber);
        return new ResponseEntity<>(savedBarber, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(@PathVariable Long id, @RequestBody Barber barberDetails) {
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

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBarber(@PathVariable Long id) {
        if (barberRepo.existsById(id)) {
            barberRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
