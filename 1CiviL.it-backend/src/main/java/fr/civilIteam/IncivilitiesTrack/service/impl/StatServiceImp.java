package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.Stats.*;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.repository.IReportRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IUserRepository;
import fr.civilIteam.IncivilitiesTrack.service.IStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImp implements IStatService {

    private final IReportRepository reportRepository;
    private final IUserRepository userRepository;

    @Override
    public GlobalStatsDTO getGlobalStats() {

        double minLat= 41.3337/*corse*/;
        double maxLat=51.1242/*nord*/;
        double minLon=-5.1422/*bretagne*/;
        double maxLon =9.5600/*corse*/;


        return new GlobalStatsDTO(
            getTotalReports().getValue(),
            getReportsByStatus(),
            getReportsByType(),
            getAverageReportsPerUser().getMoyenne(),
            getMaxReportsByUser().getValue(),
            getAverageReportsPerMonth().getMoyenne(),
            getAverageReportsByDayOfWeek(),
            getAverageReportsByHour(),
            countReportsByGeographicZone(minLat,maxLat,minLon,maxLon)//zone englobant la france entiere
        );
    }

    @Override
    public LongStatResponseDTO getTotalReports() {
        return new LongStatResponseDTO( reportRepository.countTotalReports());
    }

    @Override
    public Map<String, Long> getReportsByStatus() {
        return reportRepository.countReportsByStatusNative().stream()
            .collect(Collectors.toMap(
                obj -> ((Status) obj[0]).getName(),
                obj -> ((Number) obj[1]).longValue())
            );
    }

    @Override
    public Map<String, Long> getReportsByType() {
        return reportRepository.countReportsByTypeNative()
            .stream()
            .collect(Collectors.toMap(
                obj -> ((Type) obj[0]).getName(),
                obj -> ((Number) obj[1]).longValue())
            );
    }

    @Override
    public MoyenneStatResponseDTO getAverageReportsPerUser() {
        return new MoyenneStatResponseDTO( userRepository.calculateAverageReportsPerUser().orElse(0.0));
    }

    @Override
    public LongStatResponseDTO getMaxReportsByUser() {
        return new LongStatResponseDTO( userRepository.findMaxReportsByUser().orElse(0L));
    }

    @Override
    public MoyenneStatResponseDTO getAverageReportsPerMonth() {
        return new MoyenneStatResponseDTO (reportRepository.calculateAverageReportsPerMonth());
    }

    @Override
    public List<AverageReportsByDayDTO> getAverageReportsByDayOfWeek() {
        return reportRepository.calculateAverageReportsByDayOfWeekNative().stream()
            .map(obj -> new AverageReportsByDayDTO(
                    ((BigDecimal) obj[0]),
                    ((BigDecimal) obj[1])
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<AverageReportsByHourDTO> getAverageReportsByHour() {
        return reportRepository.calculateAverageReportsByHourNative().stream()
            .map(obj -> new AverageReportsByHourDTO(
                ((Number) obj[0]).intValue(),
                ((Number) obj[1]).doubleValue()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportsCountByZoneDTO> countReportsByGeographicZone( double minLat,double maxLat,double minLon,double maxLon) {
        return reportRepository.findWithinBoundingBox(minLat,maxLat,minLon,maxLon).stream()
                .map(obj -> new ReportsCountByZoneDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());
    }

}
