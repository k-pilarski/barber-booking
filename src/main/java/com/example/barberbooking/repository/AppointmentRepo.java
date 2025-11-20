package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarberId(Long barberId);
    List<Appointment> findByStatus(String status);
}