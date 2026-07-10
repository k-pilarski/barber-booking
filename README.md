# BarberBooking - Backend System

## 📖 Opis projektu
BarberBooking to nowoczesny, wydajny i w pełni skalowalny system rezerwacji wizyt stworzony dla salonów barberskich. Aplikacja backendowa stanowi serce systemu, zarządzając grafikami pracowników, procesem rezerwacji terminów przez klientów oraz zaawansowaną logiką biznesową salonu. Zapewnia wysoką niezawodność, spójność danych i natychmiastową komunikację z klientami aplikacji.

## 🛠 Stos technologiczny
- **Język:** Java 17+
- **Framework:** Spring Boot 3
- **Baza Danych:** PostgreSQL
- **Komunikacja Real-Time:** WebSockets
- **Raportowanie:** Apache POI (obsługa formatu Excel)
- **Testowanie:** JUnit 5, Mockito

## 🏗 Kluczowe rozwiązania architektoniczne
System został poddany rygorystycznej, 4-etapowej refaktoryzacji, dzięki której aplikacja spełnia najwyższe standardy inżynierii oprogramowania:

- **Ochrona przed wyścigami (Double-Booking):** Wdrożono blokady pesymistyczne na poziomie bazy danych z wykorzystaniem adnotacji `@Lock(PESSIMISTIC_WRITE)`. Rozwiązanie to całkowicie eliminuje ryzyko zarezerwowania tego samego terminu przez dwóch różnych klientów w tym samym ułamku sekundy.
- **Optymalizacja wydajności zapytań (N+1):** Zidentyfikowano i wyeliminowano problem N+1 zapytań do bazy danych dzięki zastosowaniu `@EntityGraph`. Ładowanie złożonych grafów obiektów i powiązań (np. rezerwacji wraz z usługami i pracownikami) jest teraz wysoce zoptymalizowane.
- **Centralna obsługa błędów:** Wprowadzono architekturę opartą o `GlobalExceptionHandler`, co gwarantuje przechwytywanie wyjątków na najwyższym poziomie, ustandaryzowaną odpowiedź API (np. zgodną z Problem Details) i spójne logowanie błędów.
- **Odporność integracji zewnętrznych:** Mechanizmy komunikacji z systemami zewnętrznymi zostały zabezpieczone restrykcyjnymi timeoutami (np. przy bramkach płatności), zapobiegając blokowaniu wątków i zapewniając wysoką dostępność usług.
- **Warstwa DTO:** Całkowite odseparowanie modelu encji od warstwy prezentacji z użyciem obiektów DTO, co chroni bazę danych przed modyfikacjami poprzez ataki typu Mass Assignment oraz poprawia czytelność kontraktów API.

## 🚀 Instrukcja uruchomienia

### Wymagania wstępne
- Java 17 (lub nowsza)
- Maven
- Baza danych PostgreSQL (zainstalowana lokalnie lub uruchomiona poprzez Docker)

### Uruchomienie lokalne
1. Skopiuj repozytorium projektu na swój dysk.
2. Zaktualizuj konfigurację bazy danych w pliku `src/main/resources/application.yml` (lub `application.properties`):
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/barberbooking
       username: twoj_uzytkownik
       password: twoje_haslo
   ```
3. Zbuduj i uruchom aplikację korzystając z wbudowanego wrappera Mavena:
   ```bash
   ./mvnw spring-boot:run
   ```

### Dokumentacja API (Swagger)
Po pomyślnym starcie aplikacji, interaktywny interfejs dokumentacji Swagger UI jest dostępny w przeglądarce pod adresem:
[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
