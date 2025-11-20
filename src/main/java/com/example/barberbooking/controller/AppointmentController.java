package com.example.barberbooking.controller;

import com.example.barberbooking.entity.Appointment;
import com.example.barberbooking.repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepo.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentRepo.findById(id);
        return appointment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment savedAppointment = appointmentRepo.save(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
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
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
