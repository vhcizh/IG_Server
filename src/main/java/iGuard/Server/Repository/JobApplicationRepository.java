package iGuard.Server.Repository;

import iGuard.Server.Entity.JobApplication;
import iGuard.Server.Entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Integer> {
    List<JobApplication> findByJob_JobId(Integer jobId);
    List<JobApplication>findByJob_Shelter_ShelterId(Integer shelterid);
}
