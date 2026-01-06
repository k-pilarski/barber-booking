package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberRepo extends JpaRepository<Barber, Long> {

    // 1. Zwykła metoda (Derived query) - już miałeś
    List<Barber> findBySpecialty(String specialty);

    // 2. WYMAGANIE: Użycie JPQL (@Query)
    // Pobiera fryzjerów o danej specjalizacji i sortuje ich alfabetycznie
    @Query("SELECT b FROM Barber b WHERE b.specialty = :specialty ORDER BY b.name ASC")
    List<Barber> findBySpecialtySorted(@Param("specialty") String specialty);

    // 3. WYMAGANIE: Użycie NativeQuery (Czysty SQL)
    // Wyszukuje po fragmencie nazwy używając SQL.
    // UWAGA: Upewnij się, że tabela w bazie nazywa się 'barber' (Spring domyślnie tak tworzy z klasy Barber)
    @Query(value = "SELECT * FROM barber WHERE name LIKE %:nameFragment%", nativeQuery = true)
    List<Barber> findByNameNative(@Param("nameFragment") String nameFragment);
}