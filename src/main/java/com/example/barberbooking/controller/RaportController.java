package com.example.barberbooking.controller;

import com.example.barberbooking.helper.RaportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class RaportController {

    private final RaportHelper helper;

    @GetMapping("/raport")
    public void getRaport( // Zmiana z ResponseEntity<String> na void
            @RequestParam("id") Long id,
            HttpServletResponse response) throws IOException {

        // 1. Pobieramy dane (tutaj warto podpiąć prawdziwe dane z bazy!)
        List<Map<String, Object>> daneRaportu = helper.zbudujObiektRaportu(id);
        
        // 2. Budujemy Excela
        XSSFWorkbook wb = helper.zbudujObiektPlikuXlsx(daneRaportu);

        // 3. Ustawiamy nagłówki
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=raport_" + id + ".xlsx");

        // 4. Wysyłamy plik
        try (ServletOutputStream os = response.getOutputStream()) {
            wb.write(os);
            os.flush(); // Ważne: wypchnij dane
        }
        wb.close();

        log.info("Wygenerowano i wysłano raport Excel dla id={}", id);
        // Nie zwracamy return, bo response został obsłużony ręcznie
    }
}