package iGuard.Server.Repository;

import iGuard.Server.Entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToiletRepository extends JpaRepository<Toilet, Integer> {
    @Query(value = "SELECT t.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(t.latitude)) * " +
            "cos(radians(t.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(t.latitude)))) AS distance " +
            "FROM Toilet t " +
            "ORDER BY distance ASC " +
            "LIMIT 7", nativeQuery = true)
    List<Toilet> findNearestToilets(@Param("latitude") double latitude, @Param("longitude") double longitude);
}