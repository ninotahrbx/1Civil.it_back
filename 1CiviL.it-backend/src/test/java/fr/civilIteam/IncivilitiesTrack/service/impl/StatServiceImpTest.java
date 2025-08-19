package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.Stats.*;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.repository.IReportRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatServiceImpTest {

    @InjectMocks
    private StatServiceImp statServiceImp;

    @Mock
    private  IReportRepository reportRepository;

    @Mock
    private  IUserRepository userRepository;

    @BeforeEach
    void setUp()
    {    MockitoAnnotations.openMocks(this);
        statServiceImp = new StatServiceImp(reportRepository, userRepository);

    }


    @Test
    void getGlobalStats() {
        long count = 5L;
        double minLat= 41.3337/*corse*/;
        double maxLat=51.1242/*nord*/;
        double minLon=-5.1422/*bretagne*/;
        double maxLon =9.5600/*corse*/;
        when(reportRepository.countTotalReports()).thenReturn(count);

        Status openStatus = new Status(1L, UUID.randomUUID(), "Open",null);
        Status closedStatus = new Status(2L,UUID.randomUUID(), "Closed",null );

        // Simulation de la réponse du repository
        List<Object[]> mockResultStatus = List.of(
                new Object[]{openStatus, 10L},
                new Object[]{closedStatus, 5L}
        );

        when(reportRepository.countReportsByStatusNative()).thenReturn(mockResultStatus);

        Type openType = new Type(1L,UUID.randomUUID(),"open",null);
        Type closeType = new Type(2L,UUID.randomUUID(),"close",null);

        List<Object[]> mockResultType = List.of(
                new Object[]{openType,15L},
                new Object[]{closeType,5L}
        );

        when(reportRepository.countReportsByTypeNative()).thenReturn(mockResultType);

        double moyen1 = 2.5;
        when (userRepository.calculateAverageReportsPerUser()).thenReturn(Optional.of( moyen1) );

        long count1 = 25;
        when (userRepository.findMaxReportsByUser()).thenReturn(Optional.of(count1));

        double moyen2 = 2.5;
        when (reportRepository.calculateAverageReportsPerMonth()).thenReturn(moyen2);

        List<Object[]> mockData = Arrays.asList(
                new Object[]{BigDecimal.valueOf(1), BigDecimal.valueOf(3.5)},  // Lundi, 3.5 reports en moyenne
                new Object[]{BigDecimal.valueOf(2), BigDecimal.valueOf(4.2)}   // Mardi, 4.2 reports en moyenne
        );

        // Simulation du comportement du repository
        when(reportRepository.calculateAverageReportsByDayOfWeekNative()).thenReturn(mockData);

        List<Object[]>mockData2 =Arrays.asList(

                new Object[]{(Number)6 ,25 },
                new Object[]{(Number)4,2.5}
        );

        when(reportRepository.calculateAverageReportsByHourNative()).thenReturn(mockData2);

        List<Object[]> mockData3 =Arrays.asList(
                new Object[]{"50.3 4.3",24},
                new Object[]{"51.2 4.1",10}

        );


        when (reportRepository.findWithinBoundingBox(minLat,maxLat,minLon,maxLon)).thenReturn(mockData3);

        GlobalStatsDTO response = statServiceImp.getGlobalStats();

        assertNotNull(response);
        assertEquals(5L,response.getTotalReports());
        assertEquals(2, response.getReportsByStatus().size());
        assertEquals(10L, response.getReportsByStatus().get("Open"));
        assertEquals(5L, response.getReportsByStatus().get("Closed"));
        assertEquals(2,response.getReportsByType().size());
        assertEquals(15L,response.getReportsByType().get("open"));
        assertEquals(5L,response.getReportsByType().get("close"));
        assertEquals(moyen1,response.getAverageReportsPerUser());
        assertEquals(count1,response.getMaxReportsByUser());
        assertEquals(moyen2,response.getAverageReportsPerMonth());
        assertEquals(2, response.getAverageReportsByDayOfWeek().size());  // Vérifie qu'il y a 2 jours traités

        assertEquals(BigDecimal.valueOf(1),response.getAverageReportsByDayOfWeek().get(0).getDayOfWeek());  // Vérifie le jour (Lundi)
        assertEquals(BigDecimal.valueOf(3.5), response.getAverageReportsByDayOfWeek().get(0).getAverageReports());  // Vérifie la moyenne

        assertEquals(BigDecimal.valueOf(2), response.getAverageReportsByDayOfWeek().get(1).getDayOfWeek());  // Vérifie le jour (Mardi)
        assertEquals(BigDecimal.valueOf(4.2), response.getAverageReportsByDayOfWeek().get(1).getAverageReports());  // Vérifie la moyenne
        assertEquals(2,response.getAverageReportsByHour().size());
        assertEquals(6,response.getAverageReportsByHour().get(0).getHour());
        assertEquals(25,response.getAverageReportsByHour().get(0).getAverageReports());
        assertEquals(2,response.getReportsByGeographicZone().size());
        assertEquals("50.3 4.3" ,response.getReportsByGeographicZone().get(0).getZoneName());
        assertEquals(24,response.getReportsByGeographicZone().get(0).getReportCount());


    }

    @Test
    void getTotalReports() {

     long count = 5L;

        when(reportRepository.countTotalReports()).thenReturn(count);

        LongStatResponseDTO response =statServiceImp.getTotalReports();

        assertNotNull(response);
        assertEquals(count,response.getValue());



    }

    @Test
    void getReportsByStatus() {
        // Création des objets Status mockés
        Status openStatus = new Status(1L, UUID.randomUUID(), "Open",null);
        Status closedStatus = new Status(2L,UUID.randomUUID(), "Closed",null );

        // Simulation de la réponse du repository
        List<Object[]> mockResult = List.of(
                new Object[]{openStatus, 10L},
                new Object[]{closedStatus, 5L}
        );

        when(reportRepository.countReportsByStatusNative()).thenReturn(mockResult);

        // Exécution de la méthode testée
        Map<String, Long> result = statServiceImp.getReportsByStatus();

        // Vérifications
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10L, result.get("Open"));
        assertEquals(5L, result.get("Closed"));
    }
    @Test
    void getReportsByType() {

        Type openType = new Type(1L,UUID.randomUUID(),"open",null);
        Type closeType = new Type(2L,UUID.randomUUID(),"close",null);

        List<Object[]> mockResult = List.of(
                new Object[]{openType,15L},
                new Object[]{closeType,5L}
        );

        when(reportRepository.countReportsByTypeNative()).thenReturn(mockResult);

        Map<String ,Long >result = statServiceImp.getReportsByType();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(15L,result.get("open"));
        assertEquals(5L,result.get("close"));

    }

    @Test
    void getAverageReportsPerUser() {

        double moyen = 2.5;
        when (userRepository.calculateAverageReportsPerUser()).thenReturn(Optional.of( moyen) );

        MoyenneStatResponseDTO response = statServiceImp.getAverageReportsPerUser();

        assertNotNull(response);
        assertEquals(moyen,response.getMoyenne());


    }

    @Test
    void getMaxReportsByUser() {

        long count = 25;
        when (userRepository.findMaxReportsByUser()).thenReturn(Optional.of(count));

        LongStatResponseDTO response = statServiceImp.getMaxReportsByUser();

        assertNotNull(response);
        assertEquals(count,response.getValue());

    }

    @Test
    void getAverageReportsPerMonth() {

        double moyen = 2.5;
        when (reportRepository.calculateAverageReportsPerMonth()).thenReturn(moyen);

        MoyenneStatResponseDTO response = statServiceImp.getAverageReportsPerMonth();

        assertNotNull(response);
        assertEquals(moyen,response.getMoyenne());

    }

    @Test
    void getAverageReportsByDayOfWeek() {
        // Simulation des résultats de la méthode native
        List<Object[]> mockData = Arrays.asList(
                new Object[]{BigDecimal.valueOf(1), BigDecimal.valueOf(3.5)},  // Lundi, 3.5 reports en moyenne
                new Object[]{BigDecimal.valueOf(2), BigDecimal.valueOf(4.2)}   // Mardi, 4.2 reports en moyenne
        );

        // Simulation du comportement du repository
        when(reportRepository.calculateAverageReportsByDayOfWeekNative()).thenReturn(mockData);

        // Exécution de la méthode testée
        List<AverageReportsByDayDTO> result = statServiceImp.getAverageReportsByDayOfWeek();

        // Vérifications
        assertEquals(2, result.size());  // Vérifie qu'il y a 2 jours traités

        assertEquals(BigDecimal.valueOf(1), result.get(0).getDayOfWeek());  // Vérifie le jour (Lundi)
        assertEquals(BigDecimal.valueOf(3.5), result.get(0).getAverageReports());  // Vérifie la moyenne

        assertEquals(BigDecimal.valueOf(2), result.get(1).getDayOfWeek());  // Vérifie le jour (Mardi)
        assertEquals(BigDecimal.valueOf(4.2), result.get(1).getAverageReports());  // Vérifie la moyenne
    }

    @Test
    void getAverageReportsByHour() {
        List<Object[]>mockData =Arrays.asList(

                new Object[]{(Number)6 ,25 },
                new Object[]{(Number)4,2.5}
        );

        when(reportRepository.calculateAverageReportsByHourNative()).thenReturn(mockData);
        List<AverageReportsByHourDTO> response=statServiceImp.getAverageReportsByHour();

        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals(6,response.get(0).getHour());
        assertEquals(25,response.get(0).getAverageReports());


    }

    @Test
    void countReportsByGeographicZone() {
        List<Object[]> mockData =Arrays.asList(
                new Object[]{"zone",24},
                new Object[]{"zone",10}

        );

        double minlat =50.2;
        double maxlat =55.2;
        double minlon=3.5;
        double maxlon =4.5;

        when (reportRepository.findWithinBoundingBox(minlat,maxlat,minlon,maxlon)).thenReturn(mockData);

       List< ReportsCountByZoneDTO> response =statServiceImp.countReportsByGeographicZone(minlat,maxlat,minlon,maxlon);

       System.out.println(response);
        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals("zone" ,response.get(0).getZoneName());
        assertEquals(24,response.get(0).getReportCount());


    }
}