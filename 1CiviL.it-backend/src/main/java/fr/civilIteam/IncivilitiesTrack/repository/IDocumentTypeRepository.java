package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IDocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    Optional<DocumentType> findByNameIgnoreCase(String name);
    Optional<DocumentType> findByUuid(UUID uuid);
    boolean existsByDocuments_DocumentType_Uuid(UUID uuid);
}
