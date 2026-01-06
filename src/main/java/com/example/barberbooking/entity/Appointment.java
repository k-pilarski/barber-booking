package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Log4j2
@Table(name = "appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status {
        SCHEDULED, CANCELLED, COMPLETED
    }

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

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_phone", nullable = false)
    private String clientPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<NotificationLog> notifications;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<ExternalNotification> externalNotifications;

    public Appointment() {
        this.status = Status.SCHEDULED;
        this.createdAt = LocalDateTime.now();

        log.debug("New Appointment created with default status={} and createdAt={}",
                this.status, this.createdAt);
    }
}
