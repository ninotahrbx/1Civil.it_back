package fr.civilIteam.IncivilitiesTrack.dto.Stats;

import lombok.*;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class AverageReportsByDayDTO {

    private final BigDecimal dayOfWeek;
    private final BigDecimal averageReports;


}
