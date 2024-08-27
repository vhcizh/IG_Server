package iGuard.Server.Repository;

import iGuard.Server.Entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository  extends JpaRepository<Place,String> {
    List<Place> findByLatitudeBetweenAndLongitudeBetween(float latMin, float latMax, float lonMin, float lonMax);
}
