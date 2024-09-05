package iGuard.Server.Repository;


import iGuard.Server.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("customJobRepository")
public interface JobRepository extends JpaRepository<Job,Integer> {
}
