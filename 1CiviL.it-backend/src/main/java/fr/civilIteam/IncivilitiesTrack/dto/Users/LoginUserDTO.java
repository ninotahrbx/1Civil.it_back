package fr.civilIteam.IncivilitiesTrack.dto.Users;

import java.util.Date;
import java.util.UUID;

public record LoginUserDTO(
        UUID uuid,
        String name,
        String firstname,
        String email,
        String phone,
        Date dateConnect
) {

}
