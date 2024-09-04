package iGuard.Server.Repository;

import iGuard.Server.Entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer> {
    @Query(value = "SELECT s.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) AS distance " +
            "FROM Shelter s " +
            "ORDER BY distance ASC " +
            "LIMIT 5", nativeQuery = true)
    List<Shelter> findNearestShelters(@Param("latitude") double latitude, @Param("longitude") double longitude);

}
