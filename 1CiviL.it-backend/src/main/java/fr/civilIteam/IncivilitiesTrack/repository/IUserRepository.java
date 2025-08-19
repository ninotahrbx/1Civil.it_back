package fr.civilIteam.IncivilitiesTrack.repository;

import fr.civilIteam.IncivilitiesTrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    List<User> findByRole_Uuid(UUID uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmailAndUuidNot(String email, UUID uuid);

    Optional<User> findByTokenPassword(String tokenPassword);

    Optional<User> findByTokenValidate(String tokenValidate);

    @Query("SELECT MAX(reportCount) FROM (SELECT COUNT(r) AS reportCount FROM User u LEFT JOIN u.reports r GROUP BY u)")
    Optional<Long> findMaxReportsByUser();

    @Query("SELECT AVG(reportCount) FROM (SELECT COUNT(r) AS reportCount FROM User u LEFT JOIN u.reports r GROUP BY u)")
    Optional<Double> calculateAverageReportsPerUser();

}
