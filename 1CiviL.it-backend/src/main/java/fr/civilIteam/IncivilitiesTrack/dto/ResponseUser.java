package fr.civilIteam.IncivilitiesTrack.dto;

import fr.civilIteam.IncivilitiesTrack.models.Role;

import java.util.UUID;

public record ResponseUser(

    UUID uuid,
    String name,
    String firstName,
    String email,
    String phone,
    String role_name

) {}
