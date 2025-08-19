package fr.civilIteam.IncivilitiesTrack.dto.mapper;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IDocumentTypeMapper {
    IDocumentTypeMapper INSTANCE = Mappers.getMapper(IDocumentTypeMapper.class);
    ResponseDocumentType documentTypeToResponseDocumentType(DocumentType documentType);
    DocumentType requestDocumentToDocumentType(RequestDocumentType requestDocumentType);
}
