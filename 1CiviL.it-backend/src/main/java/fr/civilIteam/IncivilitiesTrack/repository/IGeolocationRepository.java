package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IGeolocationRepository extends JpaRepository<Geolocation, Long> {

    Optional<Geolocation> findByUuid(UUID geolocationUuid);
  
}
