package fr.civilIteam.IncivilitiesTrack.dto.Stats;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GlobalStatsDTO {

    private Long totalReports;
    private Map<String, Long> reportsByStatus;
    private Map<String, Long> reportsByType;
    private Double averageReportsPerUser;
    private Long maxReportsByUser;
    private Double averageReportsPerMonth;
    private List<AverageReportsByDayDTO> averageReportsByDayOfWeek;
    private List<AverageReportsByHourDTO> averageReportsByHour;
    private List<ReportsCountByZoneDTO> reportsByGeographicZone;

    public String getFormattedSummary() {
        return "Total Reports: " + totalReports + ", Average Reports per User: " + averageReportsPerUser;
    }
}
