package fr.civilIteam.IncivilitiesTrack.dto;

import java.util.Date;
import java.util.UUID;

public record HistoryDTO(
        UUID uuid,
        Date date,
        String content
) {
}
