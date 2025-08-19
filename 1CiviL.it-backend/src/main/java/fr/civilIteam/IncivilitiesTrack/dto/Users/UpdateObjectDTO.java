package fr.civilIteam.IncivilitiesTrack.dto.Users;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateObjectDTO
{
    UUID OperatorUuid;
    UserUpdateDTO userUpdateDTO;

}
