package iGuard.Server.Repository;

import iGuard.Server.Entity.VisitedShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedShelterRepository extends JpaRepository<VisitedShelter, Integer> {

    List<VisitedShelter> findAllByUser_id(String id);

}
