package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IDocumentTypeMapper;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import fr.civilIteam.IncivilitiesTrack.repository.IDocumentTypeRepository;
import fr.civilIteam.IncivilitiesTrack.service.IDocumentTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentTypeServiceImpl implements IDocumentTypeService {

    private final IDocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(IDocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * Allow to create a document type
     * @param requestDocumentType DTO with parameters
     * @return Document Type saved with ID and UUID generated
     * @throws DocumentTypeAlreadyExistException if a document type with the same name already exist in DB
     */
    @Override
    public DocumentType addDocumentType(RequestDocumentType requestDocumentType) throws DocumentTypeAlreadyExistException {
        //vérifier si le documentType existe déja en BDD
        Optional<DocumentType> documentTypeExist = documentTypeRepository.findByNameIgnoreCase(requestDocumentType.name());
        if (documentTypeExist.isPresent()) {
            //Si c'est le cas, retourner une erreur
            throw new DocumentTypeAlreadyExistException();
        } else {
            //Sinon sauvegarder en BDD
            DocumentType documentType = IDocumentTypeMapper.INSTANCE.requestDocumentToDocumentType(requestDocumentType);
            return documentTypeRepository.save(documentType);
        }
    }

    /**
     * Allow to patch a specific document type
     * @param uuid UUID of the document type
     * @param requestDocumentType DTO with parameters
     * @return document type with modifications
     * @throws NotModifiedException if the document type's name in request already exist in DB
     * @throws DocumentTypeAlreadyExistException if the document type's name already exist in DB
     * @throws DocumentTypeNotFoundException if the document type's UUID doesn't exist in DB
     */
    @Override
    public DocumentType updateDocumentType(UUID uuid, RequestDocumentType requestDocumentType)
    throws NotModifiedException, DocumentTypeAlreadyExistException, DocumentTypeNotFoundException {
        //vérifier que l'uuid existe en BDD
        Optional<DocumentType> documentTypeExist = documentTypeRepository.findByUuid(uuid);
        if (documentTypeExist.isPresent()) {
            DocumentType existingDocumentType = documentTypeExist.get();
            // Vérifier que le documentType entré n'est pas identique à celui récupéré
            if (existingDocumentType.getName().equalsIgnoreCase(requestDocumentType.name())) {
                throw new NotModifiedException("Document Type non modifié");
            }
            // Vérifier que le type entré n'existe pas déjà en BDD
            Optional<DocumentType> documentTypeInDb = documentTypeRepository.findByNameIgnoreCase(requestDocumentType.name());
            if (documentTypeInDb.isPresent()) {
                throw new DocumentTypeAlreadyExistException();
            }
            //Sauvegarder l'objet mis à jour en BDD
            existingDocumentType.setName(requestDocumentType.name());
            return documentTypeRepository.save(existingDocumentType);
        } else  {
            throw new DocumentTypeNotFoundException();
        }
    }

    /**
     * Allow to delete a specific document type
     * @param uuid UUID of the document type
     * @return the uuid of the deleted document type
     * @throws DocumentTypeNotFoundException if the document type's UUID doesn't exist in DB
     * @throws DocumentTypeAlreadyUsedException if the document type is present in Identity table
     */
    @Override
    public UUID deleteDocumentType(UUID uuid) throws DocumentTypeNotFoundException, DocumentTypeAlreadyUsedException {
        // Vérifier que l'uuid existe en BDD
        Optional<DocumentType> documentTypeExist = documentTypeRepository.findByUuid(uuid);
        if (documentTypeExist.isEmpty()) {
            throw new DocumentTypeNotFoundException();
        }
        // Vérifier si le documentType est déjà utilisé dans la table Identity
        boolean isDocumentTypeInIdentity = documentTypeRepository.existsByDocuments_DocumentType_Uuid(uuid);
        if (isDocumentTypeInIdentity) {
            throw new DocumentTypeAlreadyUsedException();
        }

        DocumentType documentType = documentTypeExist.get();
        documentTypeRepository.delete(documentType);

        return uuid;
    }

    /**
     * Allow to get the list of document type
     * @return the whole list of documents type
     */
    @Override
    public List<ResponseDocumentType> getAll() {
        List<ResponseDocumentType> documentTypes = new ArrayList<>();
        for(DocumentType documentType : documentTypeRepository.findAll()) {
            documentTypes.add(IDocumentTypeMapper.INSTANCE.documentTypeToResponseDocumentType(documentType));
        }
        return  documentTypes;
    }

    /**
     *  Allow to get a specific document type
     * @param uuid uuid of the document type
     * @return a single document type
     * @throws DocumentTypeNotFoundException if the document type's UUID doesn't exist in DB
     */
    @Override
    public ResponseDocumentType getSingleDocumentType(UUID uuid) throws DocumentTypeNotFoundException {
        return IDocumentTypeMapper.INSTANCE.documentTypeToResponseDocumentType(documentTypeRepository.findByUuid(uuid).orElseThrow(DocumentTypeNotFoundException::new));
    }
}
