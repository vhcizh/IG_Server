package iGuard.Server.Repository;

import iGuard.Server.Entity.VisitedShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitedShelterRepository extends JpaRepository<VisitedShelter,Integer> {
}
