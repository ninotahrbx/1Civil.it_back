package fr.civilIteam.IncivilitiesTrack.dto.Process;

import fr.civilIteam.IncivilitiesTrack.dto.Users.UserDTO;
import fr.civilIteam.IncivilitiesTrack.dto.Users.UserProcessDTO;
import fr.civilIteam.IncivilitiesTrack.models.Geolocation;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;
@RequiredArgsConstructor
public class ProcessDTO {

    public  UUID uuid ;
    public  Date dateCreation ;
    public Geolocation geolocation ;
    public Status status ;
    public Type type ;
    public UserProcessDTO author ;


    public ProcessDTO(UUID uuid, Date date, Object o, Status status, Type type, UserProcessDTO o1) {
    }
}
