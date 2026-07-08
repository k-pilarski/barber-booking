package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Override
    @EntityGraph(attributePaths = {"barber"})
    List<Appointment> findAll();

    List<Appointment> findByBarberId(Long barberId);
    
    List<Appointment> findByStatus(String status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.barber.id = :barberId " +
           "AND a.id != :excludeAppointmentId " +
           "AND a.status != 'CANCELLED' " +
           "AND a.startTime < :endTime AND a.endTime > :startTime")
    int countOverlappingAppointments(@Param("barberId") Long barberId, 
                                     @Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("excludeAppointmentId") Long excludeAppointmentId);
}