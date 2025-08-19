package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ITypeMapper {
    ITypeMapper INSTANCE = Mappers.getMapper(ITypeMapper.class);
    ResponseType typeToResponseType(Type type);
    Type requestTypeToType(RequestType requestType);
}
