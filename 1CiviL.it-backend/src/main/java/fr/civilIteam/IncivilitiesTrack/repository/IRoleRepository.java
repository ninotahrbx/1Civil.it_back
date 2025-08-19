package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByUuid(UUID uuid);

    Optional<Role> findByName(String name);

    Optional<Role> findByNameIgnoreCase(String name);

    Optional<Role> findByNameIgnoreCaseAndUuidNot(String name, UUID uuid);

}
