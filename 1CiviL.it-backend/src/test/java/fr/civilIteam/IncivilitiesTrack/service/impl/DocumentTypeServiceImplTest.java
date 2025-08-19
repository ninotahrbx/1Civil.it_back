package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import fr.civilIteam.IncivilitiesTrack.repository.IDocumentTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DocumentTypeServiceImplTest {

    @InjectMocks
    private DocumentTypeServiceImpl documentTypeService;

    @Mock
    private IDocumentTypeRepository documentTypeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDocumentType_ShouldBeOk() {
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data  = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data);
        DocumentType response = documentTypeService.addDocumentType(body);

        assertNotNull(response);
        assertEquals("testDocumentType", response.getName());
    }

    @Test
    void addDocumentType_DocumentTypeAlreadyExist() {
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data  = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data));
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data);

        assertThrows(DocumentTypeAlreadyExistException.class, ()->documentTypeService.addDocumentType(body));
    }

    @Test
    void updateDocumentType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentTypeModified");
        DocumentType data = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());
        DocumentType data_modified = new DocumentType(1L, UUID.randomUUID(), "testDocumentTypeModified", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data_modified);
        DocumentType response = documentTypeService.updateDocumentType(uuid, body);

        assertNotNull(response);
        assertEquals("testDocumentTypeModified", response.getName());
    }

    @Test
    void updateDocumentType_NotModified() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());
        DocumentType data_modified = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data_modified);

        assertThrows(NotModifiedException.class, ()->documentTypeService.updateDocumentType(uuid, body));
    }

    @Test
    void updateDocumentType_DocumentTypeAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        DocumentType data_exists = new DocumentType(1L, UUID.randomUUID(), "originalDocumentType", new ArrayList<>());
        RequestDocumentType body = new RequestDocumentType(null, "originalDocumentType");
        DocumentType data = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data_exists));
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data);

        assertThrows(DocumentTypeAlreadyExistException.class, ()->documentTypeService.updateDocumentType(uuid, body));
    }

    @Test
    void updateDocumentType_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data_modified = new DocumentType(1L, UUID.randomUUID(), "testDocumentTypeModified", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        when(documentTypeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(documentTypeRepository.save(Mockito.any(DocumentType.class))).thenReturn(data_modified);

        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeService.updateDocumentType(uuid, body));

    }

    @Test
    void deleteDocumentType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        DocumentType data = new DocumentType(1L, uuid, "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(documentTypeRepository.existsByDocuments_DocumentType_Uuid(uuid)).thenReturn(false);

        UUID response = documentTypeService.deleteDocumentType(uuid);
        assertNotNull(response);
        assertEquals(uuid, response);
    }

    @Test
    void deleteDocumentType_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeService.deleteDocumentType(uuid));
    }

    @Test
    void deleteDocumentType_DocumentTypeAlreadyUsed() {
        UUID uuid = UUID.randomUUID();
        DocumentType data = new DocumentType(1L, uuid, "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(documentTypeRepository.existsByDocuments_DocumentType_Uuid(uuid)).thenReturn(true);

        assertThrows(DocumentTypeAlreadyUsedException.class, ()-> documentTypeService.deleteDocumentType(uuid));
    }

    @Test
    void getAll_ShouldBeOk() {
        List<DocumentType> data = new ArrayList<>();

        when(documentTypeRepository.findAll()).thenReturn(data);
        List<ResponseDocumentType> response = documentTypeService.getAll();

        assertNotNull(response);
    }

    @Test
    void getSingleDocumentType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        DocumentType data = new DocumentType(1L, uuid, "testDocumentType", new ArrayList<>());

        when(documentTypeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        ResponseDocumentType response = documentTypeService.getSingleDocumentType(uuid);

        assertNotNull(response);
        assertEquals(uuid, response.uuid());
        assertEquals("testDocumentType", response.name());
    }

    @Test
    void getSingleDocumentType_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeRepository.findByUuid(uuid)).thenThrow(DocumentTypeNotFoundException.class);

        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeService.getSingleDocumentType(uuid));
    }
}