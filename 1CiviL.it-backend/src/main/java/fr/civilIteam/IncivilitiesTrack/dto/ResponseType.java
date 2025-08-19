package fr.civilIteam.IncivilitiesTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ResponseType(
        @Schema(example = "d5ccead4-7e66-4373-8444-623a03b4a850")
        UUID uuid,
        @Schema(example = "Vol à l'arraché")
        String name) {
}
