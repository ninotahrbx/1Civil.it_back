package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestDocumentType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseDocumentType;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.DocumentTypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import fr.civilIteam.IncivilitiesTrack.service.impl.DocumentTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DocumentTypeControllerTest {

    @InjectMocks
    private  DocumentTypeController documentTypeController;

    @Mock
    private DocumentTypeServiceImpl documentTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDocumentType_ShouldBeOk() {
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeService.addDocumentType(body)).thenReturn(data);

        ResponseEntity<ResponseDocumentType> response = documentTypeController.addDocumentType(body);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(body.name(), response.getBody().name());
    }

    @Test
    void addDocumentType_DocumentTypeAlreadyExist() {
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");

        when(documentTypeService.addDocumentType(body)).thenThrow(DocumentTypeAlreadyExistException.class);
        assertThrows(DocumentTypeAlreadyExistException.class, ()->documentTypeController.addDocumentType(body));
    }

    @Test
    void updateDocumentType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");
        DocumentType data = new DocumentType(1L, UUID.randomUUID(), "testDocumentType", new ArrayList<>());

        when(documentTypeService.updateDocumentType(uuid, body)).thenReturn(data);
        ResponseEntity<ResponseDocumentType> response = documentTypeController.updateDocumentType(uuid, body);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateDocumentType_NotModified() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");

        when(documentTypeService.updateDocumentType(uuid, body)).thenThrow(NotModifiedException.class);
        assertThrows(NotModifiedException.class, ()->documentTypeController.updateDocumentType(uuid, body));
    }

    @Test
    void updateDocumentType_DocumentTypeAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");

        when(documentTypeService.updateDocumentType(uuid, body)).thenThrow(DocumentTypeAlreadyExistException.class);
        assertThrows(DocumentTypeAlreadyExistException.class, ()->documentTypeController.updateDocumentType(uuid, body));
    }

    @Test
    void updateDocumentType_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestDocumentType body = new RequestDocumentType(null, "testDocumentType");

        when(documentTypeService.updateDocumentType(uuid, body)).thenThrow(DocumentTypeNotFoundException.class);
        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeController.updateDocumentType(uuid, body));
    }

    @Test
    void delete_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeService.deleteDocumentType(uuid)).thenReturn(uuid);
        ResponseEntity<String> response = documentTypeController.delete(uuid);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Document Type deleted" , response.getBody());
    }

    @Test
    void delete_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeService.deleteDocumentType(uuid)).thenThrow(DocumentTypeNotFoundException.class);

        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeController.delete(uuid));
    }

    @Test
    void delete_DocumentTypeAlreadyUsed() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeService.deleteDocumentType(uuid)).thenThrow(DocumentTypeAlreadyUsedException.class);

        assertThrows(DocumentTypeAlreadyUsedException.class, ()->documentTypeController.delete(uuid));
    }

    @Test
    void getAll_ShouldBeOk() {
        List<ResponseDocumentType> data = new ArrayList<>();
        data.add(new ResponseDocumentType(UUID.randomUUID(), "testDocumentType1"));
        data.add(new ResponseDocumentType(UUID.randomUUID(), "testDocumentType2"));
        data.add(new ResponseDocumentType(UUID.randomUUID(), "testDocumentType3"));

        when(documentTypeService.getAll()).thenReturn(data);
        ResponseEntity<List<ResponseDocumentType>> response = documentTypeController.getAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        assertEquals("testDocumentType3", response.getBody().get(2).name());
    }

    @Test
    void getSingleDocumentType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        ResponseDocumentType data = new ResponseDocumentType(uuid, "testDocumentType");

        when(documentTypeService.getSingleDocumentType(uuid)).thenReturn(data);
        ResponseEntity<ResponseDocumentType> response = documentTypeController.getSingleDocumentType(uuid);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testDocumentType", response.getBody().name());
    }

    @Test
    void getSingleDocumentType_DocumentTypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(documentTypeService.getSingleDocumentType(uuid)).thenThrow(DocumentTypeNotFoundException.class);

        assertThrows(DocumentTypeNotFoundException.class, ()->documentTypeController.getSingleDocumentType(uuid));
    }
}