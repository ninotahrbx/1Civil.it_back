package fr.civilIteam.IncivilitiesTrack.dto;

import fr.civilIteam.IncivilitiesTrack.models.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ResponseReport(
        UUID uuid,
        String img,
        String description,
        Date dateCreation,
        String author_name,
        String type_name,
        String status_name,
        Geolocation geolocation,
        List<ResponseComment> comments,
        List<ResponseHistory> histories
) {
}
