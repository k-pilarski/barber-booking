package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Log4j2
@Table(name = "time_slot")
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TimeSlot() {
        this.isAvailable = true;
        this.createdAt = LocalDateTime.now();
        log.debug("TimeSlot entity instance created: {}", this);
    }
}
