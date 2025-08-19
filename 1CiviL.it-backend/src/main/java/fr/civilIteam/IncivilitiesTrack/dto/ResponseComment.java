package fr.civilIteam.IncivilitiesTrack.dto;

import fr.civilIteam.IncivilitiesTrack.dto.Users.UserProcessDTO;

import java.util.Date;
import java.util.UUID;

public record ResponseComment(
        UUID uuid,
        Date date,
        String content,
        UserProcessDTO author
) {
}
