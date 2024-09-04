package iGuard.Server.Repository;

import iGuard.Server.Entity.Shade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShadeRepository extends JpaRepository<Shade, Integer> {
    @Query(value = "SELECT s.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) AS distance " +
            "FROM Shade s " +
            "ORDER BY distance ASC " +
            "LIMIT 7", nativeQuery = true)
    List<Shade> findNearestShades(@Param("latitude") double latitude, @Param("longitude") double longitude);
}