package fr.civilIteam.IncivilitiesTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestName(

        @Schema(example = "Citoyen")
        String name
) {
}
