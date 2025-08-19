package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.models.Type;

import java.util.List;
import java.util.UUID;

public interface ITypeService {

    Type addType(RequestType requestType);
    Type updateType(UUID uuid, RequestType requestType);
    UUID deleteType(UUID uuid);
    List<ResponseType> getAll();
    ResponseType getSingleType(UUID uuid);
}
