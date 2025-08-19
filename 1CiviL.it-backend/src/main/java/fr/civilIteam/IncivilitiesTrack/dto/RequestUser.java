package fr.civilIteam.IncivilitiesTrack.dto;

import fr.civilIteam.IncivilitiesTrack.models.Role;

import java.util.UUID;

public record RequestUser(
        UUID uuid,
        String name,
        String firstName,
        String email,
        String phone,
        Role role
) {
}
