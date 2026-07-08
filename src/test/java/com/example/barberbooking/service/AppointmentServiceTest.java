package com.example.barberbooking.service;

import com.example.barberbooking.dto.AppointmentCreateRequest;
import com.example.barberbooking.dto.AppointmentDto;
import com.example.barberbooking.entity.Appointment;
import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.repository.AppointmentRepo;
import com.example.barberbooking.repository.BarberRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private BarberRepo barberRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    private Barber testBarber;
    private AppointmentCreateRequest testRequest;

    @BeforeEach
    void setUp() {
        testBarber = new Barber();
        testBarber.setId(1L);
        testBarber.setName("Test Barber");

        testRequest = new AppointmentCreateRequest();
        testRequest.setBarberId(1L);
        testRequest.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        testRequest.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
        testRequest.setClientName("Jan Kowalski");
        testRequest.setClientPhone("123456789");
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        // given
        when(barberRepo.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(testBarber));
        when(appointmentRepo.countOverlappingAppointments(eq(1L), any(), any(), eq(-1L))).thenReturn(0);
        
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(100L);
        savedAppointment.setBarber(testBarber);
        savedAppointment.setStartTime(testRequest.getStartTime());
        savedAppointment.setEndTime(testRequest.getEndTime());
        savedAppointment.setClientName(testRequest.getClientName());
        savedAppointment.setClientPhone(testRequest.getClientPhone());

        when(appointmentRepo.save(any(Appointment.class))).thenReturn(savedAppointment);

        // when
        AppointmentDto result = appointmentService.createAppointment(testRequest);

        // then
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(1L, result.getBarberId());
        assertEquals("Jan Kowalski", result.getClientName());
        verify(appointmentRepo, times(1)).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenDoubleBooking() {
        // given
        when(barberRepo.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(testBarber));
        // symulujemy istnienie innej wizyty w tym czasie
        when(appointmentRepo.countOverlappingAppointments(eq(1L), any(), any(), eq(-1L))).thenReturn(1);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            appointmentService.createAppointment(testRequest);
        });

        assertEquals("Wybrany termin jest już zajęty przez inną wizytę.", exception.getMessage());
        verify(appointmentRepo, never()).save(any(Appointment.class));
    }
}
