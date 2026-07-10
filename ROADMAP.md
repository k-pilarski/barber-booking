# Roadmapa Rozwoju Systemu BarberBooking

Poniższy dokument definiuje strategiczne, długoterminowe cele rozwoju dla aplikacji BarberBooking. Przedstawione "Epiki" wyznaczają kierunek ewolucji systemu, skupiając się na podnoszeniu wartości biznesowej, poprawie Customer Experience oraz integracji z nowoczesnymi technologiami sprzętowymi i AI.

---

## 🚀 EPIK 1: Natywna Aplikacja Desktopowa dla Recepcji (Tauri)
**Cel biznesowy:** Zastąpienie standardowej przeglądarki wysoce wydajnym, autonomicznym panelem dla pracowników recepcji salonu, zapewniającym lepszą ergonomię i responsywność.

**Zarys architektoniczny:**
- **Technologia:** Zastosowanie frameworka **Tauri** (Rust pod maską, frontend w React/Vue lub Angularze) w celu stworzenia aplikacji desktopowej.
- **Funkcjonalności:**
  - Bardzo szybki, błyskawicznie reagujący "Kalendarz Dnia" wykorzystujący obecną infrastrukturę **WebSockets** do podglądu rezerwacji na żywo.
  - Zużycie zaledwie ułamka zasobów systemowych i pamięci RAM w porównaniu z aplikacjami opartymi na Electronie.
  - Bezpośrednia integracja z systemowymi powiadomieniami push (Windows/macOS) przy nowych rezerwacjach.
  - Wykorzystanie natywnych bibliotek do łatwiejszej i stabilniejszej integracji z peryferiami takimi jak terminale płatnicze czy lokalne drukarki fiskalne.

---

## 🌐 EPIK 2: Integracja z ekosystemem Internet of Things (IoT)
**Cel biznesowy:** Automatyzacja przepływu obsługi klienta (Customer Journey) w fizycznym salonie oraz budowa wizerunku innowacyjnego lokalu.

**Zarys architektoniczny:**
- **Technologia:** Wykorzystanie bramek MQTT, mikrokontrolerów (np. ESP32, Raspberry Pi) oraz implementacja Spring Integration / MQTT w naszym backendzie.
- **Funkcjonalności:**
  - **Automatyczny Check-in:** Beacony BLE (Bluetooth Low Energy) lub czytniki QR skanujące aplikację klienta przy wejściu. Backend automatycznie zmienia status wizyty na "Klient oczekujący" i powiadamia konkretnego barbera (via WebSockets).
  - **Smart Poczekalnia:** Integracja z wyświetlaczami (np. ekrany e-ink) zamontowanymi przy fotelach czy w poczekalni, pobierającymi dane z backendu. Ekrany mogą wyświetlać czas do następnego strzyżenia, imię klienta lub powitania.
  - **Zarządzanie środowiskiem pracy:** Potencjalna integracja z systemami smart home salonu (np. zmiana natężenia światła nad fotelem, gdy w systemie startuje usługa "Relaksujący masaż i mycie").

---

## 🧠 EPIK 3: Wdrożenie lokalnie hostowanych modeli AI (Ollama)
**Cel biznesowy:** Zwiększenie analitycznych możliwości systemu oraz automatyzacja raportowania z zachowaniem całkowitej poufności danych klientów.

**Zarys architektoniczny:**
- **Technologia:** Uruchomienie narzędzia **Ollama** lokalnie w środowisku serwerowym (lub na dedykowanej maszynie) serwującego otwarte modele LLM (np. Llama 3, Mistral), zintegrowane poprzez **Spring AI**.
- **Funkcjonalności:**
  - **Inteligentne podsumowania finansowe:** Automatyczne parsowanie surowych danych i raportów Excela (generowanych obecnie przez Apache POI) do lokalnego modelu AI, który generuje czytelne, naturalnie brzmiące narracyjne analizy biznesowe i rekomendacje dla menedżera.
  - **Analiza behawioralna klientów (AI Concierge):** Analizowanie dotychczasowych notatek o strzyżeniach i preferencjach klientów. Asystent AI potrafi podpowiedzieć barberowi rano: "Klient Jan Kowalski często prosi o poprawkę przy fade'ie – zwróć na to uwagę. Ostatnio kupił pomadę matującą, zaproponuj mu nową z tej samej linii".
  - **Samo-diagnostyka systemu (AI Ops):** Automatyczna analiza logów błędów i logów płatności w tle. Wykrywanie anomalii w czasie trwania transakcji i generowanie alertów dla administratorów z sugerowanym rozwiązaniem.

---

## 🏗 EPIK 4: Event-Driven Architecture (EDA) & Skalowanie
**Cel biznesowy:** Przygotowanie fundamentów pod wdrożenie systemu w ujęciu franczyzowym na setkach lokali.

**Zarys architektoniczny:**
- **Technologia:** Wprowadzenie Apache Kafka lub RabbitMQ, zastosowanie wzorca Transactional Outbox.
- **Funkcjonalności:**
  - Oddzielenie ciężkich procesów (generowanie raportów Excel, wysyłanie e-maili/SMS z powiadomieniami, integracje AI) do asynchronicznych workerów za pomocą kolejkowania zdarzeń (np. `ReservationCreatedEvent`).
  - Gwarancja spójności poprzez Transactional Outbox – zapis rezerwacji do tabeli w Postgresie i publikacja eventu do Kafki dzieją się w bezpiecznej transakcji, chroniąc przed utratą danych w rozproszonym środowisku.
