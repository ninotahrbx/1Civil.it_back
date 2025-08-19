package fr.civilIteam.IncivilitiesTrack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;


import java.io.Serializable;
import java.util.Date;

public record RegisterRequest (
        @Schema(example = "test@1civiliit.fr")
        @Email
        @NotNull(message = "You must have a email")
        @NotBlank(message = "You must have a email")
        String email,
        @Schema(example = "Test12345#@&")
        @NotNull(message = "You must have a password")
        @NotBlank(message = "You must have a password")
        String password,
        @Schema(example = "TEST Testouille")
        @NotNull(message = "You must have a name")
        @NotBlank(message = "You must have a name")
        String name,
        @Schema(example = "2025-02-12")
        Date register

)implements Serializable {
}
