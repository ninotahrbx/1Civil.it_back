package fr.civilIteam.IncivilitiesTrack.dto.Stats;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@RequiredArgsConstructor
public class ReportsCountByZoneDTO {

    private final String zoneName;
    private final Long reportCount;

}
