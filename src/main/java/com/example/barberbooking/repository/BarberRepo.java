package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberRepo extends JpaRepository<Barber, Long> {
    List<Barber> findBySpecialty(String specialty);

    @Query("SELECT b FROM Barber b WHERE b.specialty = :specialty ORDER BY b.name ASC")
    List<Barber> findBySpecialtySorted(@Param("specialty") String specialty);

    @Query(value = "SELECT * FROM barber WHERE name LIKE %:nameFragment%", nativeQuery = true)
    List<Barber> findByNameNative(@Param("nameFragment") String nameFragment);
}