package fr.civilIteam.IncivilitiesTrack.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ReportRequestDTO {

    private String img;
    private String description;
    private UUID authorUuid;
    private UUID typeUuid;
    private UUID statusUuid;
    private UUID geolocationUuid;

}
