package fr.civilIteam.IncivilitiesTrack.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.civilIteam.IncivilitiesTrack.models.Geolocation;
import fr.civilIteam.IncivilitiesTrack.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query("SELECT COUNT(r) FROM Report r")
    Long countTotalReports();

    @Query("SELECT r.status, COUNT(r) FROM Report r GROUP BY r.status")
    List<Object[]> countReportsByStatusNative();

    @Query("SELECT r.type, COUNT(r) FROM Report r GROUP BY r.type")
    List<Object[]> countReportsByTypeNative();

    @Query("SELECT AVG(reportCount) FROM (SELECT COUNT(r) AS reportCount FROM User u LEFT JOIN u.reports r GROUP BY u)")
    Double calculateAverageReportsPerMonth();

    @Query(value = "SELECT EXTRACT(DOW FROM date_creation) AS dayOfWeek, " +
        "COUNT(*) * 1.0 / COUNT(DISTINCT author_id) AS averageReports " + "FROM reports " +
        "GROUP BY EXTRACT(DOW FROM date_creation)", nativeQuery = true)
    List<Object[]> calculateAverageReportsByDayOfWeekNative();

    @Query(value = "SELECT EXTRACT(HOUR FROM date_creation) AS hour, " +
        "COUNT(*) * 1.0 / COUNT(DISTINCT author_id) AS averageReports " + "FROM reports " +
        "GROUP BY EXTRACT(HOUR FROM date_creation) " + "ORDER BY hour", nativeQuery = true)
    List<Object[]> calculateAverageReportsByHourNative();

    @Query(value = "SELECT CONCAT(g.latitude, ', ', g.longitude) AS zone, COUNT(r.id) AS report_count " +
        "FROM reports r " + "JOIN geolocations g ON r.geolocation_id = g.id " +
        "GROUP BY g.latitude, g.longitude " + "ORDER BY report_count DESC", nativeQuery = true)
    List<Object[]> countReportsByGeographicZoneNative();

    @Query("SELECT 'zone' zone ,COUNT(r.id) report_count FROM Report r join Geolocation g on r.geolocation.id=g.id  WHERE   g.latitude BETWEEN :minLat AND :maxLat AND g.longitude BETWEEN :minLon AND :maxLon  ")
    List<Object[]> findWithinBoundingBox(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
                                            @Param("minLon") double minLon, @Param("maxLon") double maxLon);
}
