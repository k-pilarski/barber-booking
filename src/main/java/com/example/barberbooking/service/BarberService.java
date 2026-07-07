package com.example.barberbooking.service;

import com.example.barberbooking.dto.BarberDto;
import com.example.barberbooking.entity.Barber;
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
public class BarberService {

    private final BarberRepo barberRepo;

    @Transactional(readOnly = true)
    public List<BarberDto> getAllBarbers() {
        log.info("Pobieranie listy wszystkich barberów");
        return barberRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BarberDto> getBarberById(Long id) {
        log.info("Wyszukiwanie barbera po id: {}", id);
        return barberRepo.findById(id).map(this::mapToDto);
    }

    @Transactional
    public BarberDto createBarber(BarberDto barberDto) {
        log.info("Tworzenie nowego barbera: {}", barberDto.getName());
        Barber barber = new Barber();
        barber.setName(barberDto.getName());
        barber.setSpecialty(barberDto.getSpecialty());
        barber.setPhone(barberDto.getPhone());

        Barber saved = barberRepo.save(barber);
        log.info("Utworzono barbera o id: {}", saved.getId());
        return mapToDto(saved);
    }

    @Transactional
    public Optional<BarberDto> updateBarber(Long id, BarberDto barberDto) {
        log.info("Aktualizacja barbera o id: {}", id);
        return barberRepo.findById(id).map(barber -> {
            barber.setName(barberDto.getName());
            barber.setSpecialty(barberDto.getSpecialty());
            barber.setPhone(barberDto.getPhone());
            Barber updated = barberRepo.save(barber);
            log.info("Zaktualizowano barbera o id: {}", updated.getId());
            return mapToDto(updated);
        });
    }

    @Transactional
    public boolean deleteBarber(Long id) {
        log.info("Usuwanie barbera o id: {}", id);
        if (barberRepo.existsById(id)) {
            barberRepo.deleteById(id);
            log.info("Usunięto barbera o id: {}", id);
            return true;
        }
        log.warn("Nie znaleziono barbera o id: {}", id);
        return false;
    }

    @Transactional(readOnly = true)
    public List<BarberDto> getBarbersBySpecialtySorted(String specialty) {
        log.info("Wyszukiwanie barberów według specjalności: {}", specialty);
        return barberRepo.findBySpecialtySorted(specialty).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BarberDto> searchBarbersByNameNative(String nameFragment) {
        log.info("Wyszukiwanie barberów na podstawie fragmentu nazwy: {}", nameFragment);
        return barberRepo.findByNameNative(nameFragment).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BarberDto mapToDto(Barber barber) {
        BarberDto dto = new BarberDto();
        dto.setId(barber.getId());
        dto.setName(barber.getName());
        dto.setSpecialty(barber.getSpecialty());
        dto.setPhone(barber.getPhone());
        return dto;
    }
}
