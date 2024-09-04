package iGuard.Server.Repository;

import iGuard.Server.Entity.Shade;
import iGuard.Server.Entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToiletRepository extends JpaRepository<Toilet, Integer> {
}
