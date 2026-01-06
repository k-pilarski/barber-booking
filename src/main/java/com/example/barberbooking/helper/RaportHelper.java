package com.example.barberbooking.helper;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RaportHelper {

    // Tworzy obiekt danych do raportu – przykład
    public List<Map<String, Object>> zbudujObiektRaportu(Long id) {
        // Tutaj wstawiasz logikę pobrania danych, np. z bazy lub serwisu
        // Dla przykładu zwracamy testową listę
        return List.of(
            Map.of("nazwa", "Jan Kowalski", "usługa", "Strzyżenie", "cena", 50),
            Map.of("nazwa", "Anna Nowak", "usługa", "Koloryzacja", "cena", 120)
        );
    }

    // Tworzy plik XLSX z danych
    public XSSFWorkbook zbudujObiektPlikuXlsx(List<Map<String, Object>> dane) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Raport");

        // Tworzymy nagłówki
        if (!dane.isEmpty()) {
            Row header = sheet.createRow(0);
            int col = 0;
            for (String key : dane.get(0).keySet()) {
                Cell cell = header.createCell(col++);
                cell.setCellValue(key);
            }

            // Wypełniamy dane
            for (int i = 0; i < dane.size(); i++) {
                Row row = sheet.createRow(i + 1);
                int j = 0;
                for (Object value : dane.get(i).values()) {
                    Cell cell = row.createCell(j++);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }

        return workbook;
    }
}
