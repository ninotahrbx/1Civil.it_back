package fr.civilIteam.IncivilitiesTrack.controller;

import fr.civilIteam.IncivilitiesTrack.dto.ReportRequestDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseReport;
import fr.civilIteam.IncivilitiesTrack.exception.ReportNotFoundException;
import fr.civilIteam.IncivilitiesTrack.service.IReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock private IReportService reportService;

    @InjectMocks private ReportController reportController;

    private MockMvc mockMvc;
    private UUID reportUuid;
    private ReportRequestDTO reportRequestDTO;
    private ReportResponseDTO reportResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
        reportUuid = UUID.randomUUID();
        reportRequestDTO = new ReportRequestDTO();
        reportResponseDTO = new ReportResponseDTO();
    }

    @Test
    void testCreateReport() throws Exception {
        when(reportService.createReport(any())).thenReturn(reportResponseDTO);

        mockMvc.perform(post("/reports/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reportRequestDTO)))
            .andExpect(status().isOk());

        verify(reportService).createReport(any());
    }

    @Test
    void testUpdateReport() throws Exception {
        when(reportService.updateReport(eq(reportUuid), any())).thenReturn(reportResponseDTO);

        mockMvc.perform(put("/reports/update/" + reportUuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reportRequestDTO)))
            .andExpect(status().isOk());

        verify(reportService).updateReport(eq(reportUuid), any());
    }

    @Test
    void testUpdateReport_NotFound() throws Exception {
        when(reportService.updateReport(eq(reportUuid), any())).thenThrow(new ReportNotFoundException());

        mockMvc.perform(put("/reports/update/" + reportUuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reportRequestDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReport() throws Exception {
        doNothing().when(reportService).deleteReport(reportUuid);

        mockMvc.perform(delete("/reports/delete/" + reportUuid))
            .andExpect(status().isNoContent());

        verify(reportService).deleteReport(reportUuid);
    }

//    @Test
//    void testGetReportById() throws Exception {
//        when(reportService.getReportByUuid(reportUuid)).thenReturn(reportResponseDTO);
//
//        mockMvc.perform(get("/reports/" + reportUuid))
//            .andExpect(status().isOk());
//
//        verify(reportService).getReportByUuid(reportUuid);
//    }
//
//    @Test
//    void testGetReportById_NotFound() throws Exception {
//        when(reportService.getReportByUuid(reportUuid)).thenThrow(new ReportNotFoundException());
//
//        mockMvc.perform(get("/reports/" + reportUuid))
//            .andExpect(status().isNotFound());
//    }

    @Test
    void testGetAllReports() throws Exception {
        when(reportService.getAllReports()).thenReturn(Collections.singletonList(reportResponseDTO));

        mockMvc.perform(get("/reports/display/all"))
            .andExpect(status().isOk());

        verify(reportService).getAllReports();
    }

    @Test
    void testGetByUuid() throws Exception {
        ResponseReport responseReport = new ResponseReport(
                reportUuid,
                "image.jpg",
                "Test Description",
                new Date(),
                "Author Name",
                "Type Name",
                "Status Name",
                null, // Geolocation (si nécessaire)
                Collections.emptyList(), // Comments (si nécessaire)
                Collections.emptyList() // Histories (si nécessaire)
        );
        when(reportService.getSingleReport(reportUuid)).thenReturn(responseReport);

        mockMvc.perform(get("/reports/" + reportUuid))
            .andExpect(status().isOk());

        verify(reportService).getSingleReport(reportUuid);
    }

    @Test
    void testGetByUuid_NotFound() throws Exception {
        when(reportService.getSingleReport(reportUuid)).thenThrow(new ReportNotFoundException());

        mockMvc.perform(get("/reports/" + reportUuid))
            .andExpect(status().isNotFound());
    }
}

