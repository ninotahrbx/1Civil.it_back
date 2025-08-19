package fr.civilIteam.IncivilitiesTrack.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ReportResponseDTO {

    private UUID uuid;
    private String img;
    private String description;
    private Date dateCreation;
    private String authorName;
    private String typeName;
    private String statusName;
    private String geolocationDetails;

}
