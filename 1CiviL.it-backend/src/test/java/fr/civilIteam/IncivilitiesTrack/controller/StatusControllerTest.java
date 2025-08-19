package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.ResponseStatus;
import fr.civilIteam.IncivilitiesTrack.exception.StatusNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.service.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StatusControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatusServiceImpl statusService;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statusController).build();
    }

    @Test
    void addStatus_ShouldReturnCreatedStatus() throws Exception {
        Status status = new Status(1L, UUID.randomUUID(), "testStatus", Collections.emptyList());

        when(statusService.addStatus(any())).thenReturn(status);

        mockMvc.perform(post("/status/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"testStatus\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testStatus"));
    }

    @Test
    void updateStatus_ShouldReturnUpdatedStatus() throws Exception {
        UUID uuid = UUID.randomUUID();
        Status updatedStatus = new Status(1L, uuid, "updatedStatus", Collections.emptyList());

        when(statusService.updateStatus(any(UUID.class), any())).thenReturn(updatedStatus);

        mockMvc.perform(patch("/status/update/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"updatedStatus\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updatedStatus"));
    }

    @Test
    void deleteStatus_ShouldReturnOk() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(statusService.deleteStatus(uuid)).thenReturn(uuid);

        mockMvc.perform(delete("/status/delete/{uuid}", uuid))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStatuses_ShouldReturnListOfStatuses() throws Exception {
        Status status = new Status(1L, UUID.randomUUID(), "testStatus", Collections.emptyList());

        List<ResponseStatus> listStatus = new ArrayList<>();
        listStatus.add(new ResponseStatus(UUID.randomUUID(), "testStatus1"));
        listStatus.add(new ResponseStatus(UUID.randomUUID(), "listStatus2"));
        listStatus.add(new ResponseStatus(UUID.randomUUID(), "listStatus3"));

        when(statusService.getAll()).thenReturn(listStatus);

        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testStatus1"));
    }

    @Test
    void getSingleStatus_ShouldReturnStatus() throws Exception {
        UUID uuid = UUID.randomUUID();
        Status status = new Status(1L, uuid, "testStatus", Collections.emptyList());
        ResponseStatus responseStatus = new ResponseStatus(UUID.randomUUID(), "testStatus");

        when(statusService.getSingleStatus(uuid)).thenReturn(responseStatus);

        mockMvc.perform(get("/status/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testStatus"));
    }

    @Test
    void getSingleStatus_ShouldReturnNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(statusService.getSingleStatus(uuid)).thenThrow(new StatusNotFoundException());

        mockMvc.perform(get("/status/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }
}