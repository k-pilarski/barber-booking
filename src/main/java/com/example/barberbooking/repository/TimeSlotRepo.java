package com.example.barberbooking.repository;

import com.example.barberbooking.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByBarberId(Long barberId);
    List<TimeSlot> findByBarberIdAndIsAvailableTrue(Long barberId);
    List<TimeSlot> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
