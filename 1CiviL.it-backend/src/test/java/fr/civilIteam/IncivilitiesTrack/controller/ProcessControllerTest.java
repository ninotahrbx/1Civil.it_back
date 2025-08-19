package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;
import fr.civilIteam.IncivilitiesTrack.dto.RequestUUID;
import fr.civilIteam.IncivilitiesTrack.models.Geolocation;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.models.Type;
import fr.civilIteam.IncivilitiesTrack.service.impl.ProcessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ProcessControllerTest {

    @InjectMocks
    private ProcessController processController;

    @Mock
    private ProcessServiceImpl processServiceImpl;

    @BeforeEach
    void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listProcess() {

        List<ProcessDTO> listprocessDTO = new ArrayList<>();
        /*public  UUID uuid ;
    public  Date dateCreation ;
    public Geolocation geolocation ;
    public Status status ;
    public Type type ;
    public UserProcessDTO author ;*/

        Status status = new Status(1L,UUID.randomUUID(),"Test",null);
        Type type = new Type(1L,UUID.randomUUID(),"Type Test",null);
        ProcessDTO processDTO=new ProcessDTO(UUID.randomUUID(),new Date(),null,status,type,null);
        ProcessDTO processDTO2=new ProcessDTO(UUID.randomUUID(),new Date(),null,status,type,null);

        listprocessDTO.add(processDTO);
        listprocessDTO.add(processDTO2);

        when(processServiceImpl.getAllActive()).thenReturn(listprocessDTO);

        ResponseEntity<List<ProcessDTO>> response =processController.ListProcess();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(2,response.getBody().size());

    }

    @Test
    void updateStatus_shouldBeOK() {

        UUID uuid = UUID.randomUUID();
        UUID uuidStatus = UUID.randomUUID();
        RequestUUID requestUUID=new RequestUUID(uuidStatus);

        when (processServiceImpl.UpdateStatusReport(uuid,uuidStatus)).thenReturn(true);

        ResponseEntity<String>response = processController.UpdateStatus(uuid,requestUUID);

        assertNotNull( response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());


    }
}