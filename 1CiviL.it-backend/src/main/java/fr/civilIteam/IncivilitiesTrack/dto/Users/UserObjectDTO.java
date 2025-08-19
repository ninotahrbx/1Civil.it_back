package fr.civilIteam.IncivilitiesTrack.dto.Users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class UserObjectDTO {

    private String email;
    private String password;
    private String name;
    private String first_name;
    private String phone;
}
