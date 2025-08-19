package fr.civilIteam.IncivilitiesTrack.service;

import fr.civilIteam.IncivilitiesTrack.dto.ReportRequestDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseReport;

import java.util.List;
import java.util.UUID;

public interface IReportService {

    ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO);

    void deleteReport(UUID uuid);

    ReportResponseDTO updateReport(UUID uuid, ReportRequestDTO reportRequestDTO);

    ReportResponseDTO getReportByUuid(UUID uuid);

    List<ReportResponseDTO> getAllReports();

    ResponseReport getSingleReport(UUID uuid);
}
