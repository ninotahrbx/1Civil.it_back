package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.Stats.*;

import java.util.List;
import java.util.Map;

public interface IStatService {

    GlobalStatsDTO getGlobalStats();
    LongStatResponseDTO getTotalReports();
    Map<String, Long> getReportsByStatus();
    Map<String, Long> getReportsByType();
    MoyenneStatResponseDTO getAverageReportsPerUser();
    LongStatResponseDTO getMaxReportsByUser();
    MoyenneStatResponseDTO getAverageReportsPerMonth();
    List<AverageReportsByDayDTO> getAverageReportsByDayOfWeek();
    List<AverageReportsByHourDTO> getAverageReportsByHour();
    List<ReportsCountByZoneDTO> countReportsByGeographicZone(double minLat,double maxLat,double minLon,double maxLon);


}
