package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BarberRepo extends JpaRepository<Barber, Long> {
    List<Barber> findByNameContaining(String name);
    List<Barber> findBySpecialty(String specialty);
}