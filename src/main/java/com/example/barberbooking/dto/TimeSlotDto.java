package com.example.barberbooking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimeSlotDto {
    private Long id;
    private Long barberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAvailable;
}
