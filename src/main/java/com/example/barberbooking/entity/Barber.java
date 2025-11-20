package com.example.barberbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
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
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Barber() {
    }
}
