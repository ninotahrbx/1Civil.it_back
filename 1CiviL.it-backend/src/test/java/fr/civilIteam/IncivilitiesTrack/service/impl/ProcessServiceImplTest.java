package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;
import fr.civilIteam.IncivilitiesTrack.exception.ReportNotFoundException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.*;
import fr.civilIteam.IncivilitiesTrack.repository.IPRocessRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IReportRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
 class ProcessServiceImplTest {

    @InjectMocks
    private ProcessServiceImpl processServiceImpl;

    @Mock
    private IPRocessRepository ipRocessRepository;

    @Mock
    private IReportRepository reportRepository;

    @Mock
    private IStatusRepository statusRepository;


    private String STATUSENCOURT ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        STATUSENCOURT=UUID.randomUUID().toString();
        processServiceImpl=new ProcessServiceImpl(ipRocessRepository,reportRepository,statusRepository,STATUSENCOURT);
    }

    @Test
    public void getAll_shouldBeOK ()
    {


        Long id =1L;
        UUID uuid =UUID.randomUUID();
        List<Report> reports = new ArrayList<>();
        reports.add(new Report(null, UUID.randomUUID(),"False img","test",new Date(),null,null,new Status(1L,uuid,"En cours",null ),null,null,null));

        reports.add(new Report(null,UUID.randomUUID(),"False img","test2",new Date(),null,null,new Status(1L,uuid,"En cours",null ),null,null,null));

        reports.add(new Report(null,UUID.randomUUID(),"False img","test3",new Date(),null,null,new Status(1L,uuid,"En cours",null ),null,null,null));


        when (ipRocessRepository.findByStatus_Uuid(UUID.fromString(STATUSENCOURT))).thenReturn(reports);

        List<ProcessDTO> response = processServiceImpl.getAllActive();

        assertNotNull(response);
        assertEquals(3,response.size());
        assertEquals(uuid,response.get(0).status.getUuid());
    }

    @Test
    public void updateStatusReport_shouldBeOK()
    {

        UUID uuidProcess = UUID.randomUUID();
        UUID uuidStatus = UUID.randomUUID();
        User user =new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", "admin", "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);

        Report report = new Report(1L,uuidProcess,null,"description",new Date(),user,new Type(1L,UUID.randomUUID(),"test",null),new Status(1L,UUID.randomUUID(),"test",null),null,null,null);
       Status status = new Status(1L,UUID.randomUUID(),"TEST",null);
        when(reportRepository.findByUuid(uuidProcess)).thenReturn(Optional.of(report));
        when(statusRepository.findByUuid(uuidStatus)).thenReturn(Optional.of(status));

        Boolean response =processServiceImpl.UpdateStatusReport(uuidProcess,uuidStatus);

        assertNotNull(response);
        assertEquals(true,response);


    }
       @Test
    public void updateStatusReport_shouldbeReportNotFound ()
    {
        UUID uuidProcess = UUID.randomUUID();

         UUID uuidStatus = UUID.randomUUID();
         User user =new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", "admin", "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);
         Report report = new Report(1L,uuidProcess,null,"description",new Date(),user,new Type(1L,UUID.randomUUID(),"test",null),new Status(1L,UUID.randomUUID(),"test",null),null,null,null);

         when (reportRepository.findByUuid(uuidProcess)).thenThrow(ReportNotFoundException.class);

         assertThrows(ReportNotFoundException.class,()->processServiceImpl.UpdateStatusReport(uuidProcess,uuidStatus))       ;
    }

    @Test
    public void updateStatusReport_shouldBeStatusNotFound  ()
    {
          UUID uuidProcess = UUID.randomUUID();
          UUID uuidStatus = UUID.randomUUID();
          User user =new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", "admin", "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null);
          Report report = new Report(1L,uuidProcess,null,"description",new Date(),user,new Type(1L,UUID.randomUUID(),"test",null),new Status(1L,UUID.randomUUID(),"test",null),null,null,null);
             Status status = new Status(1L,UUID.randomUUID(),"TEST",null);
      when(reportRepository.findByUuid(uuidProcess)).thenReturn(Optional.of(report));
         when (statusRepository.findByUuid(uuidStatus)).thenThrow(StatusNotFoundException.class);

         assertThrows(StatusNotFoundException.class,()->processServiceImpl.UpdateStatusReport(uuidProcess,uuidStatus));

    }


 



}