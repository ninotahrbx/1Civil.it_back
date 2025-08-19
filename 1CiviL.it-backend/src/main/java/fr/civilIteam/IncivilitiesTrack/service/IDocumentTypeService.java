package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;

import java.util.List;
import java.util.UUID;

public interface IDocumentTypeService {

    DocumentType addDocumentType(RequestDocumentType requestDocumentType);
    DocumentType updateDocumentType(UUID uuid, RequestDocumentType requestDocumentType);
    UUID deleteDocumentType(UUID uuid);
    List<ResponseDocumentType> getAll();
    ResponseDocumentType getSingleDocumentType(UUID uuid);
}
