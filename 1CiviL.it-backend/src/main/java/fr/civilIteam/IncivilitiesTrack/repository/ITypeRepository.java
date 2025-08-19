package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByNameIgnoreCase(String name);
    Optional<Type> findByUuid(UUID uuid);
    boolean existsByReports_Uuid(UUID uuid);
}
