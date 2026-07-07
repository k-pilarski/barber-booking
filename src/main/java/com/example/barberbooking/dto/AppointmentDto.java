package com.example.barberbooking.dto;

import com.example.barberbooking.entity.Appointment.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDto {
    private Long id;
    private Long barberId;
    private String barberName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String clientName;
    private String clientPhone;
    private Status status;
    private LocalDateTime createdAt;
}
