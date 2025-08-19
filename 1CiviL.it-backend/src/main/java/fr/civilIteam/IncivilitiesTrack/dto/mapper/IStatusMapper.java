package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.*;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IStatusMapper {

    IStatusMapper INSTANCE = Mappers.getMapper(IStatusMapper.class);
    ResponseStatus statusToResponseStatus (Status status);
    Status requestStatusToStatus (RequestStatus status);
}
