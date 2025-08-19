package fr.civilIteam.IncivilitiesTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @Schema(example = "client@daltons.fr")
        @NotNull(message = "You must have a username")
        @NotBlank(message = "You must have a username")
        String email,
        @Schema(example = "Test12345#@&")
        @NotNull(message = "You must have a password")
        @NotBlank(message = "You must have a password")
        String password)
 {
}
