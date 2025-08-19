package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.ReportRequestDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseReport;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IReportMapper;
import fr.civilIteam.IncivilitiesTrack.exception.ReportNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.*;
import fr.civilIteam.IncivilitiesTrack.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock private IReportRepository reportRepository;
    @Mock private IUserRepository userRepository;
    @Mock private ITypeRepository typeRepository;
    @Mock private IStatusRepository statusRepository;
    @Mock private IGeolocationRepository geolocationRepository;
    @Mock private IReportMapper reportMapper;

    @InjectMocks private ReportServiceImpl reportService;

    private UUID reportUuid;
    private Report report;
    private ReportRequestDTO reportRequestDTO;
    private ReportResponseDTO reportResponseDTO;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
        reportUuid = UUID.randomUUID();
        report = new Report();
        report.setUuid(reportUuid);
        report.setImg("image.jpg");
        report.setDescription("Test Description");
        report.setDateCreation(new Date());
        report.setAuthor(new User());
        report.setType(new Type());
        report.setStatus(new Status());
        report.setGeolocation(null);
        report.setComments(new ArrayList<>());
        report.setHistories(new ArrayList<>());

        reportRequestDTO = new ReportRequestDTO();
        reportRequestDTO.setDescription("Updated Description");
        reportResponseDTO = new ReportResponseDTO();
    }
  
    @Test
    public void testGetReportService_Success() {

        UUID uuid = UUID.randomUUID();
        Report report = new Report();
        report.setUuid(uuid);
        report.setImg("image.jpg");
        report.setDescription("Test Description");
        report.setDateCreation(new Date());
        report.setAuthor(new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", "admin", "admin","admin","0600000000",null,null,new Date(),null,null ,new Role(2L,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null),null ,null,null,null,null));
        report.setType(new Type(null,null,"Agression",null));
        report.setStatus(new Status(null,null,"StatusTest",null));
        report.setGeolocation(new Geolocation(null,UUID.randomUUID(),230.5,453.7,null));
        report.setComments(new ArrayList<>());
        report.setHistories(new ArrayList<>());

        when(reportRepository.findByUuid(uuid)).thenReturn(Optional.of(report));

        ResponseReport responseDTO= reportService.getSingleReport(uuid);

        assertNotNull(responseDTO);
        assertEquals(uuid, responseDTO.uuid());
        assertEquals("image.jpg", responseDTO.img());

        verify(reportRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testCreateReport() {
        when(userRepository.findByUuid(any())).thenReturn(Optional.of(new User()));
        when(typeRepository.findByUuid(any())).thenReturn(Optional.of(new Type()));
        when(statusRepository.findByUuid(any())).thenReturn(Optional.of(new Status()));
        when(geolocationRepository.findByUuid(any())).thenReturn(Optional.of(new Geolocation()));
        when(reportRepository.save(any())).thenReturn(report);
        when(reportMapper.toResponseDTO(any())).thenReturn(reportResponseDTO);

        ReportResponseDTO result = reportService.createReport(reportRequestDTO);
        assertNotNull(result);
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void testUpdateReport() {
        when(reportRepository.findByUuid(reportUuid)).thenReturn(Optional.of(report));
        when(typeRepository.findByUuid(any())).thenReturn(Optional.of(new Type()));
        when(statusRepository.findByUuid(any())).thenReturn(Optional.of(new Status()));
        when(geolocationRepository.findByUuid(any())).thenReturn(Optional.of(new Geolocation()));
        when(reportRepository.save(any())).thenReturn(report);
        when(reportMapper.toResponseDTO(any())).thenReturn(reportResponseDTO);

        ReportResponseDTO result = reportService.updateReport(reportUuid, reportRequestDTO);
        assertNotNull(result);
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void testDeleteReport() {
        doNothing().when(reportRepository).deleteByUuid(any());
        reportService.deleteReport(reportUuid);
        verify(reportRepository).deleteByUuid(reportUuid);
    }

    @Test
    void testGetReportByUuid() {
        when(reportRepository.findByUuid(reportUuid)).thenReturn(Optional.of(report));
        when(reportMapper.toResponseDTO(any())).thenReturn(reportResponseDTO);

        ReportResponseDTO result = reportService.getReportByUuid(reportUuid);
        assertNotNull(result);
        verify(reportRepository).findByUuid(reportUuid);
    }

    @Test
    void testGetAllReports() {
        when(reportRepository.findAll()).thenReturn(List.of(report));
        when(reportMapper.toResponseDTO(any())).thenReturn(reportResponseDTO);

        List<ReportResponseDTO> results = reportService.getAllReports();
        assertFalse(results.isEmpty());
        verify(reportRepository).findAll();
    }

    @Test
    void testGetSingleReport() {
        when(reportRepository.findByUuid(reportUuid)).thenReturn(Optional.of(report));
        ResponseReport responseReport = new ResponseReport(reportUuid, "img.png", "desc", new Date(), "user", "type", "status", null, null, null);

        ResponseReport result = reportService.getSingleReport(reportUuid);
        assertNotNull(result);
        verify(reportRepository).findByUuid(reportUuid);
    }

    @Test
    void testGetSingleReport_NotFound() {
        when(reportRepository.findByUuid(reportUuid)).thenReturn(Optional.empty());
        assertThrows(ReportNotFoundException.class, () -> reportService.getSingleReport(reportUuid));
    }
}
