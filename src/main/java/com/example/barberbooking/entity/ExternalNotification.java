package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Log4j2
@Table(name = "external_notification")
public class ExternalNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "target_service", nullable = false, length = 50)
    private String targetService;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "status", length = 20)
    private String status; // SUCCESS / FAILED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ExternalNotification() {
        this.createdAt = LocalDateTime.now();
        log.debug("Utworzono instancję encji ExternalNotification");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalNotification)) return false;
        ExternalNotification that = (ExternalNotification) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
