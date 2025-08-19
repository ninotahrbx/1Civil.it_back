package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseReport;
import fr.civilIteam.IncivilitiesTrack.service.impl.ReportServiceImpl;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import fr.civilIteam.IncivilitiesTrack.dto.ReportRequestDTO;
import fr.civilIteam.IncivilitiesTrack.service.IReportService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final IReportService reportService;

    @Autowired
    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        return ResponseEntity.ok(reportService.createReport(reportRequestDTO));
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<ReportResponseDTO> updateReport(@PathVariable UUID uuid, @RequestBody ReportRequestDTO reportRequestDTO) {
        return ResponseEntity.ok(reportService.updateReport(uuid, reportRequestDTO));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID uuid) {
        reportService.deleteReport(uuid);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("display/{uuid}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(reportService.getReportByUuid(uuid));
    }*/

    @GetMapping("/display/all")
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseReport> getByuuid(@PathVariable UUID uuid ) {
        return ResponseEntity.ok(reportService.getSingleReport(uuid));
    }
}
