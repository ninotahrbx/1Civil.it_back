package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.service.impl.TypeServiceImpl;
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

class TypeControllerTest {

    @InjectMocks
    private TypeController typeController;

    @Mock
    private TypeServiceImpl typeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addType_ShouldBeOk() {
        RequestType body = new RequestType(null, "testType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeService.addType(body)).thenReturn(data);

        ResponseEntity<ResponseType> response = typeController.addType(body);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(body.name(), response.getBody().name());
    }

    @Test
    void addType_TypeAlreadyExist() {
        RequestType body = new RequestType(null, "testType");

        when(typeService.addType(body)).thenThrow(TypeAlreadyExistException.class);
        assertThrows(TypeAlreadyExistException.class, ()->typeController.addType(body));
    }

    @Test
    void updateType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeService.updateType(uuid, body)).thenReturn(data);
        ResponseEntity<ResponseType> response = typeController.updateType(uuid,body);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(body.name(), response.getBody().name());
    }

    @Test
    void updateType_NotModified() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");

        when(typeService.updateType(uuid, body)).thenThrow(NotModifiedException.class);
        assertThrows(NotModifiedException.class, ()->typeController.updateType(uuid, body));
    }

    @Test
    void updateType_TypeAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");

        when(typeService.updateType(uuid, body)).thenThrow(TypeAlreadyExistException.class);
        assertThrows(TypeAlreadyExistException.class, ()->typeController.updateType(uuid, body));
    }

    @Test
    void updateType_TypeNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");

        when(typeService.updateType(uuid, body)).thenThrow(TypeNotFoundException.class);
        assertThrows(TypeNotFoundException.class, ()->typeController.updateType(uuid, body));
    }

    @Test
    void delete_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();

        when(typeService.deleteType(uuid)).thenReturn(uuid);
        ResponseEntity<String> response = typeController.delete(uuid);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type deleted", response.getBody());
    }

    @Test
    void delete_TypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(typeService.deleteType(uuid)).thenThrow(TypeNotFoundException.class);

        assertThrows(TypeNotFoundException.class, ()->typeController.delete(uuid));
    }

    @Test
    void delete_TypeAlreadyUsed() {
        UUID uuid = UUID.randomUUID();

        when(typeService.deleteType(uuid)).thenThrow(TypeAlreadyUsedException.class);

        assertThrows(TypeAlreadyUsedException.class, ()->typeController.delete(uuid));
    }

    @Test
    void getAll_ShouldBeOk() {
        List<ResponseType> data = new ArrayList<>();
        data.add(new ResponseType(UUID.randomUUID(), "testType1"));
        data.add(new ResponseType(UUID.randomUUID(), "testType2"));
        data.add(new ResponseType(UUID.randomUUID(), "testType3"));

        when(typeService.getAll()).thenReturn(data);
        ResponseEntity<List<ResponseType>> response = typeController.getAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(3,response.getBody().size());
        assertEquals("testType3",response.getBody().get(2).name());
    }

    @Test
    void getSingleType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        ResponseType data = new ResponseType(uuid, "testType");

        when(typeService.getSingleType(uuid)).thenReturn(data);
        ResponseEntity<ResponseType> response = typeController.getSingleType(uuid);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("testType",response.getBody().name());
    }

    @Test
    void getSingleType_TypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(typeService.getSingleType(uuid)).thenThrow(TypeNotFoundException.class);

        assertThrows(TypeNotFoundException.class, ()->typeController.getSingleType(uuid));
    }
}