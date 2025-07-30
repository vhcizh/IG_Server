package iGuard.Server.Repository;

import iGuard.Server.Entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer>, ShelterRepositoryCustom {

    @Query(value = "SELECT s FROM Shelter s WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * " +
            "cos(radians(s.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(s.latitude)))) < :range")
    List<Shelter> findSheltersWithinRange(@Param("lat") float lat,
                                      @Param("lon") float lon,
                                      @Param("range") float range);

    @Query("SELECT DISTINCT s.facilityTypeName FROM Shelter s")
    List<String> findDistinctFacilityTypes();

    @Query("SELECT DISTINCT SUBSTRING(s.address, 1, LOCATE(' ', s.address) - 1) FROM Shelter s")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT SUBSTRING(s.address, LOCATE(' ', s.address) + 1, LOCATE(' ', s.address, LOCATE(' ', s.address) + 1) - LOCATE(' ', s.address) - 1) FROM Shelter s WHERE s.address LIKE CONCAT(?1, '%')")
    List<String> findDistinctGusByCity(String city);

    @Query("SELECT DISTINCT SUBSTRING(s.address, LOCATE(' ', s.address, LOCATE(' ', s.address) + 1) + 1) FROM Shelter s WHERE s.address LIKE ?1 AND s.address LIKE '%동'")
    List<String> findDongsByCityAndGu(String addressPattern);

    @Query("SELECT CONCAT(s.shelterName, '|', s.address) FROM Shelter s")
    List<String> findAllUniqueKeys();

    boolean existsByShelterNameAndAddress(String shelterName, String address);

    Optional<Shelter> findByShelterId(int shelterid);

}