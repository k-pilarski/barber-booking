package com.example.barberbooking.service;

import com.example.barberbooking.dto.TimeSlotDto;
import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.entity.TimeSlot;
import com.example.barberbooking.repository.BarberRepo;
import com.example.barberbooking.repository.TimeSlotRepo;
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
public class TimeSlotService {

    private final TimeSlotRepo timeSlotRepo;
    private final BarberRepo barberRepo;

    @Transactional(readOnly = true)
    public List<TimeSlotDto> getAllTimeSlots() {
        log.info("Pobieranie wszystkich dostępnych przedziałów czasowych");
        return timeSlotRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TimeSlotDto> getTimeSlotById(Long id) {
        log.info("Wyszukiwanie przedziału czasowego po id: {}", id);
        return timeSlotRepo.findById(id).map(this::mapToDto);
    }

    @Transactional
    public TimeSlotDto createTimeSlot(TimeSlotDto dto) {
        log.info("Tworzenie nowego przedziału czasowego dla barbera id: {}", dto.getBarberId());
        Barber barber = barberRepo.findById(dto.getBarberId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono barbera o id: " + dto.getBarberId()));

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setBarber(barber);
        timeSlot.setStartTime(dto.getStartTime());
        timeSlot.setEndTime(dto.getEndTime());
        timeSlot.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);

        TimeSlot saved = timeSlotRepo.save(timeSlot);
        log.info("Utworzono przedział czasowy o id: {}", saved.getId());
        return mapToDto(saved);
    }

    @Transactional
    public Optional<TimeSlotDto> updateTimeSlot(Long id, TimeSlotDto dto) {
        log.info("Aktualizacja przedziału czasowego o id: {}", id);
        return timeSlotRepo.findById(id).map(timeSlot -> {
            Barber barber = barberRepo.findById(dto.getBarberId())
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono barbera o id: " + dto.getBarberId()));
            
            timeSlot.setBarber(barber);
            timeSlot.setStartTime(dto.getStartTime());
            timeSlot.setEndTime(dto.getEndTime());
            timeSlot.setIsAvailable(dto.getIsAvailable());
            
            TimeSlot updated = timeSlotRepo.save(timeSlot);
            log.info("Zaktualizowano przedział czasowy o id: {}", updated.getId());
            return mapToDto(updated);
        });
    }

    @Transactional
    public boolean deleteTimeSlot(Long id) {
        log.info("Usuwanie przedziału czasowego o id: {}", id);
        if (timeSlotRepo.existsById(id)) {
            timeSlotRepo.deleteById(id);
            log.info("Usunięto przedział czasowy o id: {}", id);
            return true;
        }
        log.warn("Nie znaleziono przedziału czasowego o id: {}", id);
        return false;
    }

    private TimeSlotDto mapToDto(TimeSlot timeSlot) {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setId(timeSlot.getId());
        if (timeSlot.getBarber() != null) {
            dto.setBarberId(timeSlot.getBarber().getId());
        }
        dto.setStartTime(timeSlot.getStartTime());
        dto.setEndTime(timeSlot.getEndTime());
        dto.setIsAvailable(timeSlot.getIsAvailable());
        return dto;
    }
}
