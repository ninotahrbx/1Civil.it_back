package fr.civilIteam.IncivilitiesTrack.dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ReportDTO(
        UUID uuid,
        String img,
        String description,
        Date dateCreation,
        UUID authorUuid,
        UUID typeUuid,
        UUID statusUuid,
        UUID geolocationUuid,
        List<CommentDTO> comments,
        List<HistoryDTO> histories
) {
}