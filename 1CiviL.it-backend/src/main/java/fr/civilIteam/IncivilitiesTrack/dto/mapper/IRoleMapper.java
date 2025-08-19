package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.RequestName;
import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseRole;
import fr.civilIteam.IncivilitiesTrack.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IRoleMapper {

    IRoleMapper INSTANCE = Mappers.getMapper(IRoleMapper.class);

    ResponseRole roleToResponseRole (Role role );
    Role requestNameToRole (RequestName dto);
    Role RequestUUIDToRole (RequestUUID dto);
}
