package fr.civilIteam.IncivilitiesTrack.dto.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ZoneGeoStatDTO {

    double minLat;
    double maxLat;
    double minLon;
    double maxLon;

}
