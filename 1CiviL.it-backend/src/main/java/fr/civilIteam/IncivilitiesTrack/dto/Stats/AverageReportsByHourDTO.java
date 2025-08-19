package fr.civilIteam.IncivilitiesTrack.dto.Stats;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AverageReportsByHourDTO {

    private final Integer hour;
    private final Double averageReports;

}
