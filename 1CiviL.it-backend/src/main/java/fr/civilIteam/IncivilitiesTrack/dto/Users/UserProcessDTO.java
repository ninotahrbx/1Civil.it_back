package fr.civilIteam.IncivilitiesTrack.dto.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserProcessDTO {

    private UUID uuid;
    private String name;
    private String first_name;

}
