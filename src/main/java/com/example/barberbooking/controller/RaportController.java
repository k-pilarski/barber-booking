package com.example.barberbooking.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barberbooking.helper.RaportHelper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class RaportController {

    private final RaportHelper helper;

    @GetMapping("/raport")
    public void getRaport(
            @RequestParam("id") Long id,
            HttpServletResponse response) throws IOException {

        List<Map<String, Object>> daneRaportu = helper.zbudujObiektRaportu(id);
        
        XSSFWorkbook wb = helper.zbudujObiektPlikuXlsx(daneRaportu);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=raport_" + id + ".xlsx");

        try (ServletOutputStream os = response.getOutputStream()) {
            wb.write(os);
            os.flush();
        }
        wb.close();

        log.info("Excel report generated and sent for id={}", id);
    }
}