package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IPRocessRepository extends  JpaRepository <Report,Long>{


    List<Report> findByStatus_Id(Long id);

    List<Report> findByStatus_Uuid(UUID uuid);
}
