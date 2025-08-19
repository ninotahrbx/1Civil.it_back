package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.ReportRequestDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ReportResponseDTO;
import fr.civilIteam.IncivilitiesTrack.dto.ResponseReport;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IReportMapper;
import fr.civilIteam.IncivilitiesTrack.exception.ReportNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Report;
import fr.civilIteam.IncivilitiesTrack.repository.*;
import fr.civilIteam.IncivilitiesTrack.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired private IReportRepository reportRepository;
    @Autowired private IUserRepository userRepository;
    @Autowired private ITypeRepository typeRepository;
    @Autowired private IStatusRepository statusRepository;
    @Autowired private IGeolocationRepository geolocationRepository;
    @Autowired
    private IReportMapper reportMapper;

    @Autowired
    public ReportServiceImpl(IReportRepository reportRepository, IUserRepository userRepository, ITypeRepository typeRepository,
        IStatusRepository statusRepository, IGeolocationRepository geolocationRepository, IReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.statusRepository = statusRepository;
        this.geolocationRepository = geolocationRepository;
        this.reportMapper = reportMapper;

    }

    @Override
    public ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO) {
        Report report = new Report();
        report.setImg(reportRequestDTO.getImg());
        report.setDescription(reportRequestDTO.getDescription());
        report.setDateCreation(new Date());
        report.setAuthor(userRepository.findByUuid(reportRequestDTO.getAuthorUuid()).orElseThrow());
        report.setType(typeRepository.findByUuid(reportRequestDTO.getTypeUuid()).orElseThrow());
        report.setStatus(statusRepository.findByUuid(reportRequestDTO.getStatusUuid()).orElseThrow());
        report.setGeolocation(geolocationRepository.findByUuid(reportRequestDTO.getGeolocationUuid()).orElseThrow());

        return reportMapper.toResponseDTO(reportRepository.save(report));
    }

    @Override
    public ReportResponseDTO updateReport(UUID uuid, ReportRequestDTO reportRequestDTO) {
        Report report = reportRepository.findByUuid(uuid).orElseThrow();
        report.setImg(reportRequestDTO.getImg());
        report.setDescription(reportRequestDTO.getDescription());
        report.setType(typeRepository.findByUuid(reportRequestDTO.getTypeUuid()).orElseThrow());
        report.setStatus(statusRepository.findByUuid(reportRequestDTO.getStatusUuid()).orElseThrow());
        report.setGeolocation(geolocationRepository.findByUuid(reportRequestDTO.getGeolocationUuid()).orElseThrow());

        return reportMapper.toResponseDTO(reportRepository.save(report));
    }

    @Override
    public void deleteReport(UUID uuid) {
        reportRepository.deleteByUuid(uuid);
    }

    @Override
    public ReportResponseDTO getReportByUuid(UUID uuid) {
        return reportRepository.findByUuid(uuid)
            .map(reportMapper::toResponseDTO)
            .orElseThrow(() -> new RuntimeException("Report not found with UUID: " + uuid));
    }

    @Override
    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAll().stream()
            .map(reportMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Allow to get a specific report
     * @param uuid UUID of the report
     * @return a single report
     * @throws ReportNotFoundException if the report's UUID doesn't exist in DB
     */
    @Override
    public ResponseReport getSingleReport(UUID uuid) throws ReportNotFoundException {
        Report report = reportRepository.findByUuid(uuid).orElseThrow(ReportNotFoundException::new);
        return IReportMapper.INSTANCE.entityToResponse(report);
    }
}
