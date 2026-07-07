package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@Log4j2
@Table(name = "barber")
public class Barber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "specialty", length = 50)
    private String specialty;

    @Column(name = "phone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    public Barber() {
        log.debug("Utworzono instancję encji Barber");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Barber)) return false;
        Barber barber = (Barber) o;
        return id != null && id.equals(barber.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
