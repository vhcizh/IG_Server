package iGuard.Server.Repository;

import iGuard.Server.Entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer>, ShelterRepositoryCustom {

    @Query(value = "SELECT s.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(s.latitude)))) AS distance " +
            "FROM Shelter s " +
            "ORDER BY distance ASC " +
            "LIMIT 5", nativeQuery = true)
    List<Shelter> findNearestShelters(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query("SELECT DISTINCT s.facilityTypeName FROM Shelter s")
    List<String> findDistinctFacilityTypes();

    @Query("SELECT DISTINCT SUBSTRING(s.address, 1, LOCATE(' ', s.address) - 1) FROM Shelter s")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT SUBSTRING(s.address, LOCATE(' ', s.address) + 1, LOCATE(' ', s.address, LOCATE(' ', s.address) + 1) - LOCATE(' ', s.address) - 1) FROM Shelter s WHERE s.address LIKE CONCAT(?1, '%')")
    List<String> findDistinctGusByCity(String city);

    @Query("SELECT DISTINCT SUBSTRING(s.address, LOCATE(' ', s.address, LOCATE(' ', s.address) + 1) + 1) FROM Shelter s WHERE s.address LIKE ?1 AND s.address LIKE '%동'")
    List<String> findDongsByCityAndGu(String addressPattern);


    boolean existsByShelterNameAndAddress(String shelterName, String address);
}