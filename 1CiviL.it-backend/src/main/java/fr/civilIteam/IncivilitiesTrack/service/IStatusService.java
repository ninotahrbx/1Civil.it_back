package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.RequestStatus;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseStatus;
import fr.civilIteam.IncivilitiesTrack.models.Status;


import java.util.List;
import java.util.UUID;


public interface IStatusService {
    Status addStatus(RequestStatus requestStatus);
    Status updateStatus(UUID uuid, RequestStatus requestStatus);
    UUID deleteStatus(UUID uuid);
    List<ResponseStatus> getAll();
    ResponseStatus getSingleStatus(UUID uuid);
}
