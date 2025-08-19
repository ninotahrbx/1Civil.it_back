package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestType;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseType;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.TypeNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.repository.ITypeRepository;
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

class TypeServiceImplTest {

    @InjectMocks
    private TypeServiceImpl typeService;

    @Mock
    private ITypeRepository typeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addType_ShouldBeOk() {
        RequestType body = new RequestType(null, "testType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data);
        Type response = typeService.addType(body);

        assertNotNull(response);
        assertEquals("testType", response.getName());
    }

    @Test
    void addType_TypeAlreadyExist() {
        RequestType body = new RequestType(null, "testType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data));
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data);

        assertThrows(TypeAlreadyExistException.class, ()->typeService.addType(body));
    }

    @Test
    void updateType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testTypeModified");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());
        Type data_modified = new Type(1L, UUID.randomUUID(), "testTypeModified", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data_modified);
        Type response = typeService.updateType(uuid, body);

        assertNotNull(response);
        assertEquals("testTypeModified",response.getName());
    }

    @Test
    void updateType_NotModified() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());
        Type data_modified = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data_modified);

        assertThrows(NotModifiedException.class, ()->typeService.updateType(uuid, body));
    }

    @Test
    void updateType_TypeAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        Type data_exists = new Type(1L, UUID.randomUUID(), "originalType", new ArrayList<>());
        RequestType body = new RequestType(null, "originalType");
        Type data = new Type(1L, UUID.randomUUID(), "testType", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data_exists));
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data);

        assertThrows(TypeAlreadyExistException.class, ()->typeService.updateType(uuid, body));
    }

    @Test
    void updateType_TypeNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestType body = new RequestType(null, "testType");
        Type data_modified = new Type(1L, UUID.randomUUID(), "testTypeModified", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        when(typeRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(typeRepository.save(Mockito.any(Type.class))).thenReturn(data_modified);

        assertThrows(TypeNotFoundException.class, ()->typeService.updateType(uuid, body));
    }

    @Test
    void deleteType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        Type data = new Type(1L, uuid, "testType", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(typeRepository.existsByReports_Uuid(uuid)).thenReturn(false);

        UUID response = typeService.deleteType(uuid);
        assertNotNull(response);
        assertEquals(uuid, response);
    }

    @Test
    void deleteType_TypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(TypeNotFoundException.class, () -> typeService.deleteType(uuid));
    }

    @Test
    void deleteType_TypeAlreadyUsed() {
        UUID uuid = UUID.randomUUID();
        Type data = new Type(1L, uuid, "testType", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(typeRepository.existsByReports_Uuid(uuid)).thenReturn(true);

        assertThrows(TypeAlreadyUsedException.class, () -> typeService.deleteType(uuid));
    }


    @Test
    void getAll_ShouldBeOk() {
        List<Type> data = new ArrayList<>();

        when(typeRepository.findAll()).thenReturn(data);
        List<ResponseType> response = typeService.getAll();

        assertNotNull(response);
    }

    @Test
    void getSingleType_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        Type data = new Type(1L, uuid, "testType", new ArrayList<>());

        when(typeRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        ResponseType response = typeService.getSingleType(uuid);

        assertNotNull(response);
        assertEquals(uuid,response.uuid());
        assertEquals("testType", response.name());
    }

    @Test
    void getSingleType_TypeNotFound() {
        UUID uuid = UUID.randomUUID();

        when(typeRepository.findByUuid(uuid)).thenThrow(TypeNotFoundException.class);

        assertThrows(TypeNotFoundException.class, ()->typeService.getSingleType(uuid));
    }
}