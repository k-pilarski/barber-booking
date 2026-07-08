package com.example.barberbooking.service;

import com.example.barberbooking.dto.AppointmentCreateRequest;
import com.example.barberbooking.dto.AppointmentDto;
import com.example.barberbooking.entity.Appointment;
import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.repository.AppointmentRepo;
import com.example.barberbooking.repository.BarberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final BarberRepo barberRepo;

    @Transactional(readOnly = true)
    public List<AppointmentDto> getAllAppointments() {
        log.info("Pobieranie wszystkich wizyt (zoptymalizowane Eager/EntityGraph)");
        return appointmentRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AppointmentDto> getAppointmentById(Long id) {
        log.info("Wyszukiwanie wizyty po id: {}", id);
        return appointmentRepo.findById(id).map(this::mapToDto);
    }

    @Transactional
    public AppointmentDto createAppointment(AppointmentCreateRequest request) {
        log.info("Tworzenie nowej wizyty dla barbera id: {} w czasie od {} do {}", 
                request.getBarberId(), request.getStartTime(), request.getEndTime());
        
        // 1. Zablokowanie Barbera (PESSIMISTIC_WRITE) na czas sprawdzania i zapisu wizyty
        Barber barber = barberRepo.findByIdWithPessimisticLock(request.getBarberId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono barbera o id: " + request.getBarberId()));

        // 2. Walidacja kolizji terminów (Double-Booking), dla nowej wizyty wykluczamy id = -1
        int overlapping = appointmentRepo.countOverlappingAppointments(
                barber.getId(), request.getStartTime(), request.getEndTime(), -1L);

        if (overlapping > 0) {
            log.warn("Odrzucono rezerwację z powodu nakładających się terminów (barber id: {})", barber.getId());
            throw new IllegalStateException("Wybrany termin jest już zajęty przez inną wizytę.");
        }

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setClientName(request.getClientName());
        appointment.setClientPhone(request.getClientPhone());

        Appointment saved = appointmentRepo.save(appointment);
        log.info("Utworzono i bezpiecznie zapisano wizytę o id: {}", saved.getId());
        
        return mapToDto(saved);
    }

    @Transactional
    public Optional<AppointmentDto> updateAppointment(Long id, AppointmentCreateRequest request) {
        log.info("Aktualizacja wizyty o id: {}", id);
        return appointmentRepo.findById(id).map(appointment -> {
            
            // Zablokowanie Barbera pesymistycznie
            Barber barber = barberRepo.findByIdWithPessimisticLock(request.getBarberId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono barbera o id: " + request.getBarberId()));
            
            // Walidacja kolizji terminów (wykluczamy aktualizowaną wizytę po ID)
            int overlapping = appointmentRepo.countOverlappingAppointments(
                barber.getId(), request.getStartTime(), request.getEndTime(), id);
                                    
            if (overlapping > 0) {
                 throw new IllegalStateException("Nowy termin jest już zajęty przez inną wizytę.");
            }
            
            appointment.setBarber(barber);
            appointment.setStartTime(request.getStartTime());
            appointment.setEndTime(request.getEndTime());
            appointment.setClientName(request.getClientName());
            appointment.setClientPhone(request.getClientPhone());
            
            Appointment updated = appointmentRepo.save(appointment);
            log.info("Zaktualizowano wizytę o id: {}", updated.getId());
            return mapToDto(updated);
        });
    }

    @Transactional
    public boolean deleteAppointment(Long id) {
        log.info("Usuwanie wizyty o id: {}", id);
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            log.info("Usunięto wizytę o id: {}", id);
            return true;
        }
        log.warn("Nie znaleziono wizyty do usunięcia o id: {}", id);
        return false;
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        if (appointment.getBarber() != null) {
            dto.setBarberId(appointment.getBarber().getId());
            dto.setBarberName(appointment.getBarber().getName());
        }
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setClientName(appointment.getClientName());
        dto.setClientPhone(appointment.getClientPhone());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());
        return dto;
    }
}
