package iGuard.Server.Repository;

import iGuard.Server.Entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository  extends JpaRepository<Place,String>, PlaceRepositoryCustom {
    @Query(value = "SELECT p FROM Place p WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:lon)) + sin(radians(:lat)) * " +
            "sin(radians(p.latitude)))) < :range")
    List<Place> findPlacesWithinRange(@Param("lat") float lat,
                                      @Param("lon") float lon,
                                      @Param("range") float range);


    boolean existsByNameAndAddress(String name, String address);
}
