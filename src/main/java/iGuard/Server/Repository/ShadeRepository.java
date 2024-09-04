package iGuard.Server.Repository;

import iGuard.Server.Entity.Shade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShadeRepository extends JpaRepository<Shade, Integer> {

}
