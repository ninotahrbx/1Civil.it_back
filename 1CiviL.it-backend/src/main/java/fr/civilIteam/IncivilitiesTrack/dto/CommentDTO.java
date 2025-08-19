package fr.civilIteam.IncivilitiesTrack.dto;

import jakarta.persistence.Column;

import java.util.Date;
import java.util.UUID;

public record CommentDTO(
        UUID uuid,
        Date date,
        String content
) {
}
