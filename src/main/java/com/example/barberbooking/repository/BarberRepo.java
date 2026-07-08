package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Barber;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarberRepo extends JpaRepository<Barber, Long> {
    
    List<Barber> findBySpecialty(String specialty);

    @Query("SELECT b FROM Barber b WHERE b.specialty = :specialty ORDER BY b.name ASC")
    List<Barber> findBySpecialtySorted(@Param("specialty") String specialty);

    @Query(value = "SELECT * FROM barber WHERE name LIKE %:nameFragment%", nativeQuery = true)
    List<Barber> findByNameNative(@Param("nameFragment") String nameFragment);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Barber b WHERE b.id = :id")
    Optional<Barber> findByIdWithPessimisticLock(@Param("id") Long id);
}