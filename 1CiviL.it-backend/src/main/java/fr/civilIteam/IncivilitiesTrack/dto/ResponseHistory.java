package fr.civilIteam.IncivilitiesTrack.dto;

import jakarta.persistence.Column;

import java.util.UUID;

public record ResponseHistory(
        UUID uuid,
        String content,
        String dateLog
) {
}
