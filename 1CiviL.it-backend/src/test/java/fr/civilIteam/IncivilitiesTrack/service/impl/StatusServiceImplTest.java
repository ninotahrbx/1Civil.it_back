package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.RequestStatus;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseStatus;
import fr.civilIteam.IncivilitiesTrack.exception.NotModifiedException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusAlreadyExistException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusAlreadyUsedException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.repository.IStatusRepository;
import fr.civilIteam.IncivilitiesTrack.service.impl.StatusServiceImpl;
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

class StatusServiceImplTest {

    @InjectMocks
    private StatusServiceImpl statusService;

    @Mock
    private IStatusRepository statusRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStatus_ShouldBeOk() {
        RequestStatus body = new RequestStatus(null, "testStatus");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());

        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data);
        Status response = statusService.addStatus(body);

        assertNotNull(response);
        assertEquals("testStatus", response.getName());
    }

    @Test
    void addStatus_StatusAlreadyExist() {
        RequestStatus body = new RequestStatus(null, "testStatus");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());

        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data));
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data);

        assertThrows(StatusAlreadyExistException.class, ()->statusService.addStatus(body));
    }

    @Test
    void updateStatus_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        RequestStatus body = new RequestStatus(null, "testStatusModified");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());
        Status data_modified = new Status(1L, UUID.randomUUID(), "testStatusModified", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data_modified);
        Status response = statusService.updateStatus(uuid, body);

        assertNotNull(response);
        assertEquals("testStatusModified",response.getName());
    }

    @Test
    void updateStatus_NotModified() {
        UUID uuid = UUID.randomUUID();
        RequestStatus body = new RequestStatus(null, "testStatus");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());
        Status data_modified = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data_modified);

        assertThrows(NotModifiedException.class, ()->statusService.updateStatus(uuid, body));
    }

    @Test
    void updateStatus_StatusAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        Status data_exists = new Status(1L, UUID.randomUUID(), "originalStatus", new ArrayList<>());
        RequestStatus body = new RequestStatus(null, "originalStatus");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.of(data_exists));
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data);

        assertThrows(StatusAlreadyExistException.class, ()->statusService.updateStatus(uuid, body));
    }

    @Test
    void updateStatus_StatusNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestStatus body = new RequestStatus(null, "testStatus");
        Status data = new Status(1L, UUID.randomUUID(), "testStatus", new ArrayList<>());
        Status data_modified = new Status(1L, UUID.randomUUID(), "testStatusModified", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        when(statusRepository.findByNameIgnoreCase(body.name())).thenReturn(Optional.empty());
        when(statusRepository.save(Mockito.any(Status.class))).thenReturn(data_modified);

        assertThrows(StatusNotFoundException.class, ()->statusService.updateStatus(uuid, body));
    }

    @Test
    void deleteStatus_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        Status data = new Status(1L, uuid, "testStatus", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(statusRepository.findByReports_Uuid(uuid)).thenReturn(Optional.empty());

        UUID response = statusService.deleteStatus(uuid);
        assertNotNull(response);
        assertEquals(uuid, response);
    }

    @Test
    void deleteStatus_StatusNotFound() {
        UUID uuid = UUID.randomUUID();

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class, () -> statusService.deleteStatus(uuid));
    }

    @Test
    void deleteStatus_StatusAlreadyUsed() {
        UUID uuid = UUID.randomUUID();
        Status data = new Status(1L, uuid, "testStatus", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        when(statusRepository.findByReports_Uuid(uuid)).thenReturn(Optional.of(data));

        assertThrows(StatusAlreadyUsedException.class, () -> statusService.deleteStatus(uuid));
    }

    @Test
    void getAll_ShouldBeOk() {
        List<Status> data = new ArrayList<>();

        when(statusRepository.findAll()).thenReturn(data);
        List<ResponseStatus> response = statusService.getAll();

        assertNotNull(response);
    }

    @Test
    void getSingleStatus_ShouldBeOk() {
        UUID uuid = UUID.randomUUID();
        Status data = new Status(1L, uuid, "testStatus", new ArrayList<>());

        when(statusRepository.findByUuid(uuid)).thenReturn(Optional.of(data));
        ResponseStatus response = statusService.getSingleStatus(uuid);

        assertNotNull(response);
        assertEquals(uuid,response.uuid());
        assertEquals("testStatus", response.name());
    }

    @Test
    void getSingleStatus_StatusNotFound() {
        UUID uuid = UUID.randomUUID();

        when(statusRepository.findByUuid(uuid)).thenThrow(StatusNotFoundException.class);

        assertThrows(StatusNotFoundException.class, ()->statusService.getSingleStatus(uuid));
    }
}