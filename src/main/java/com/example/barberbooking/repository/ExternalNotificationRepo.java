package com.example.barberbooking.repository;

import com.example.barberbooking.entity.ExternalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExternalNotificationRepo extends JpaRepository<ExternalNotification, Long> {
    List<ExternalNotification> findByAppointmentId(Long appointmentId);
}
