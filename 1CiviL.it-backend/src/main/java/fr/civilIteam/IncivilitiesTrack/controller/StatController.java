package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.Stats.*;
import fr.civilIteam.IncivilitiesTrack.service.impl.StatServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistiques")
@RequiredArgsConstructor
public class StatController {

    private final StatServiceImp statService;

    @Secured("admin")
    @GetMapping("/globals")
    public ResponseEntity<GlobalStatsDTO> getGlobalStats() {
        return ResponseEntity.ok(statService.getGlobalStats());
    }

    @Secured("admin")
    @GetMapping("/total-reports")
    public ResponseEntity<LongStatResponseDTO> getTotalReports() {
        return ResponseEntity.ok(statService.getTotalReports());
    }

    @Secured("admin")
    @GetMapping("/reports-by-status")
    public ResponseEntity<Map<String ,Long >> getReportsByStatus() {
        return ResponseEntity.ok(statService.getReportsByStatus());
    }

    @Secured("admin")
    @GetMapping("/reports-by-type")
    public ResponseEntity<Map<String, Long>> getReportsByType() {
        return ResponseEntity.ok(statService.getReportsByType());
    }

    @Secured("admin")
    @GetMapping("/average-reports-per-user")
    public ResponseEntity<MoyenneStatResponseDTO> getAverageReportsPerUser() {
        return ResponseEntity.ok(statService.getAverageReportsPerUser());
    }

    @Secured("admin")
    @GetMapping("/max-reports-by-user")
    public ResponseEntity<LongStatResponseDTO> getMaxReportsByUser() {
        return ResponseEntity.ok(statService.getMaxReportsByUser());
    }

    @Secured("admin")
    @GetMapping("/average-reports-per-month")
    public ResponseEntity<MoyenneStatResponseDTO> getAverageReportsPerMonth() {
        return ResponseEntity.ok(statService.getAverageReportsPerMonth());
    }

    @Secured("admin")
    @GetMapping("/reportsByDay")
    public ResponseEntity<List<AverageReportsByDayDTO>> getAverageReportsByDayOfWeek() {
        List<AverageReportsByDayDTO> stats = statService.getAverageReportsByDayOfWeek();
        return ResponseEntity.ok(stats);
    }

    @Secured("admin")
    @GetMapping("/reportsByHour")
    public ResponseEntity<List<AverageReportsByHourDTO>> getAverageReportsByHour() {
        List<AverageReportsByHourDTO> stats = statService.getAverageReportsByHour();
        return ResponseEntity.ok(stats);
    }

    @Secured("admin")
    @GetMapping("/reportsByZone")
    public ResponseEntity<List<ReportsCountByZoneDTO>> countReportsByGeographicZone(@RequestBody ZoneGeoStatDTO zoneGeoStatDTO) {
        List<ReportsCountByZoneDTO> stats = statService.countReportsByGeographicZone(zoneGeoStatDTO.getMinLat(), zoneGeoStatDTO.getMaxLat(), zoneGeoStatDTO.getMinLon(),zoneGeoStatDTO.getMaxLon());
        return ResponseEntity.ok(stats);
    }
}
