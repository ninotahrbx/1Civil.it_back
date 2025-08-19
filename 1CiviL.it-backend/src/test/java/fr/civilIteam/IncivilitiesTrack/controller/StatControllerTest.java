package fr.civilIteam.IncivilitiesTrack.controller;


import fr.civilIteam.IncivilitiesTrack.dto.Stats.*;
import fr.civilIteam.IncivilitiesTrack.service.IStatService;
import fr.civilIteam.IncivilitiesTrack.service.impl.StatServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatControllerTest {

    @Mock
    private StatServiceImp statService;

    @InjectMocks
    private StatController statController;

    private GlobalStatsDTO globalStatsDTO;
    private MoyenneStatResponseDTO moyenneStatResponseDTO;
    private LongStatResponseDTO longStatResponseDTO;
    private List<AverageReportsByDayDTO> averageReportsByDay;
    private List<AverageReportsByHourDTO> averageReportsByHour;
    private List<ReportsCountByZoneDTO> reportsByZone;
    private Map<String, Long> reportsByStatus;
    private Map<String, Long> reportsByType;
    private long totalReports;

    @BeforeEach
    void setUp() {
        globalStatsDTO = new GlobalStatsDTO(
            80L,
            Map.of("Open", 50L, "Closed", 30L),
            Map.of("Vandalism", 20L, "Littering", 40L),
            2.5,
            10L,
            5.2,
            List.of(new AverageReportsByDayDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(5.2))),
            List.of(new AverageReportsByHourDTO(14, 3.5)),
            List.of(new ReportsCountByZoneDTO("Paris", 100L))
        );

        moyenneStatResponseDTO = new MoyenneStatResponseDTO(2.5);
        longStatResponseDTO = new LongStatResponseDTO(10L);
        averageReportsByDay = List.of(new AverageReportsByDayDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(5.2)));
        averageReportsByHour = List.of(new AverageReportsByHourDTO(14, 3.5));
        reportsByZone = List.of(new ReportsCountByZoneDTO("Paris", 100L));
        reportsByStatus = Map.of("Open", 50L, "Closed", 30L);
        reportsByType = Map.of("Vandalism", 20L, "Littering", 40L);
        totalReports = 80L;
    }

    @Test
    void getGlobalStats() {
        when(statService.getReportsByStatus()).thenReturn(reportsByStatus);
        ResponseEntity<Map<String, Long>> response = statController.getReportsByStatus();
        assertEquals(reportsByStatus, response.getBody());

    }

    @Test
    void getTotalReports() {

        LongStatResponseDTO totalReports = new LongStatResponseDTO(6L);
        when(statService.getTotalReports()).thenReturn(totalReports);
        ResponseEntity<LongStatResponseDTO> response = statController.getTotalReports();
        assertEquals(totalReports.getValue(), response.getBody().getValue());

    }

    @Test
    void getReportsByStatus() {

        when(statService.getReportsByStatus()).thenReturn(reportsByStatus);
        ResponseEntity<Map<String, Long>> response = statController.getReportsByStatus();
        assertEquals(reportsByStatus, response.getBody());

    }

    @Test
    void getReportsByType() {

        when(statService.getReportsByType()).thenReturn(reportsByType);
        ResponseEntity<Map<String, Long>> response = statController.getReportsByType();
        assertEquals(reportsByType, response.getBody());

    }

    @Test
    void getAverageReportsPerUser() {

        when(statService.getAverageReportsPerUser()).thenReturn(moyenneStatResponseDTO);
        ResponseEntity<MoyenneStatResponseDTO> response = statController.getAverageReportsPerUser();
        assertEquals(moyenneStatResponseDTO, response.getBody());

    }

    @Test
    void getMaxReportsByUser() {

        when(statService.getMaxReportsByUser()).thenReturn(longStatResponseDTO);
        ResponseEntity<LongStatResponseDTO> response = statController.getMaxReportsByUser();
        assertEquals(longStatResponseDTO, response.getBody());

    }

    @Test
    void getAverageReportsPerMonth() {

        when(statService.getAverageReportsPerMonth()).thenReturn(moyenneStatResponseDTO);
        ResponseEntity<MoyenneStatResponseDTO> response = statController.getAverageReportsPerMonth();
        assertEquals(moyenneStatResponseDTO, response.getBody());

    }

    @Test
    void getAverageReportsByDayOfWeek() {

        when(statService.getAverageReportsByDayOfWeek()).thenReturn(averageReportsByDay);
        ResponseEntity<List<AverageReportsByDayDTO>> response = statController.getAverageReportsByDayOfWeek();
        assertEquals(averageReportsByDay, response.getBody());

    }

    @Test
    void getAverageReportsByHour() {

        when(statService.getAverageReportsByHour()).thenReturn(averageReportsByHour);
        ResponseEntity<List<AverageReportsByHourDTO>> response = statController.getAverageReportsByHour();
        assertEquals(averageReportsByHour, response.getBody());

    }

   @Test
    void countReportsByGeographicZone() {

       double minlat =50.2;
       double maxlat =55.2;
       double minlon=3.5;
       double maxlon =4.5;

       ZoneGeoStatDTO zoneGeoStatDTO=new ZoneGeoStatDTO(minlat,maxlat,minlon,maxlon);

        when(statService.countReportsByGeographicZone(minlat,maxlat,minlon,maxlon)).thenReturn(reportsByZone);
        ResponseEntity<List<ReportsCountByZoneDTO>> response = statController.countReportsByGeographicZone(zoneGeoStatDTO);
        assertEquals(reportsByZone, response.getBody());

    }
}