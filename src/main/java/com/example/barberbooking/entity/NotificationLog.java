package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Log4j2
@Table(name = "notification_log")
public class NotificationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "type", nullable = false, length = 20)
    private String type; // WebSocket, Email, SMS

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "status", length = 20)
    private String status; // SUCCESS, FAILED

    public NotificationLog() {
        log.debug("NotificationLog entity instance created");
    }
}
