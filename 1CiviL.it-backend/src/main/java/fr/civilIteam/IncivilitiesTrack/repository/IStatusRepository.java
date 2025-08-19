package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IStatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByNameIgnoreCase(String name);
    Optional<Status> findByUuid(UUID uuid);
    Optional<Status> findByReports_Uuid(UUID uuid);
}
