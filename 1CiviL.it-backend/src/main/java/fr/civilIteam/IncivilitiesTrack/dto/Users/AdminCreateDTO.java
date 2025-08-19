package fr.civilIteam.IncivilitiesTrack.dto.Users;

import fr.civilIteam.IncivilitiesTrack.models.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDTO extends  UserCreateDTO{


    private Role role;

}
