package com.example.barberbooking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentCreateRequest {

    @NotNull(message = "ID barbera jest wymagane")
    private Long barberId;

    @NotNull(message = "Czas rozpoczęcia jest wymagany")
    @FutureOrPresent(message = "Czas rozpoczęcia musi być w przyszłości lub teraźniejszości")
    private LocalDateTime startTime;

    @NotNull(message = "Czas zakończenia jest wymagany")
    @FutureOrPresent(message = "Czas zakończenia musi być w przyszłości lub teraźniejszości")
    private LocalDateTime endTime;

    @NotBlank(message = "Imię klienta nie może być puste")
    private String clientName;

    @NotBlank(message = "Telefon klienta nie może być pusty")
    private String clientPhone;
}
