package fr.civilIteam.IncivilitiesTrack.service.impl;

import fr.civilIteam.IncivilitiesTrack.dto.Process.ProcessDTO;
import fr.civilIteam.IncivilitiesTrack.dto.mapper.IProcessMapper;
import fr.civilIteam.IncivilitiesTrack.exception.ReportNotFoundException;
import fr.civilIteam.IncivilitiesTrack.exception.StatusNotFoundException;
import fr.civilIteam.IncivilitiesTrack.models.Report;
import fr.civilIteam.IncivilitiesTrack.models.Status;
import fr.civilIteam.IncivilitiesTrack.repository.IPRocessRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IReportRepository;
import fr.civilIteam.IncivilitiesTrack.repository.IStatusRepository;
import fr.civilIteam.IncivilitiesTrack.service.IPRocessService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Service
public class ProcessServiceImpl implements IPRocessService {

    private final IPRocessRepository processRepository;
    private final IReportRepository reportRepository;
    private final IStatusRepository statusRepository;
    @Value("${STATUSENCOUR}")
    private String STATUSENCOURT ;

    @Override
    public List<ProcessDTO> getAllActive() {

        UUID encourtUuid = UUID.fromString(STATUSENCOURT);



        return processRepository.findByStatus_Uuid(encourtUuid)
                .stream()
                .map(IProcessMapper.INSTANCE::reportToProcessDto)
                .toList();
    }

    @Override
    public boolean UpdateStatusReport(UUID uuidProcess, UUID uuidStatus) {

        Report report =reportRepository.findByUuid(uuidProcess).orElseThrow(ReportNotFoundException::new);
        Status status =statusRepository.findByUuid(uuidStatus).orElseThrow(StatusNotFoundException::new);

        report.setStatus(status);

        reportRepository.save(report);


        return true;
    }
}
