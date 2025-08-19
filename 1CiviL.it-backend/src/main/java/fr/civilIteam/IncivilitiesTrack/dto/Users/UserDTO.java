package fr.civilIteam.IncivilitiesTrack.dto.Users;

import fr.civilIteam.IncivilitiesTrack.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private UUID uuid;
    private String email;
    private String name;
    private String first_name;
    private String phone;
    private Date dateCreation;
    private Date dateConnect;
    private Date dateModify;
    private Role role;

}
