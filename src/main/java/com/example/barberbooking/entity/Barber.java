package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
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
    @ToString.Exclude
    @JsonIgnore
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Appointment> appointments;

    public Barber() {
        log.debug("Barber entity instance created");
    }
}
