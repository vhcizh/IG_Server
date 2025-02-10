package iGuard.Server.Service;


import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.JobApplication;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Entity.User;
import iGuard.Server.Enum.JobType;
import iGuard.Server.Repository.JobApplicationRepository;
import iGuard.Server.Repository.JobRepository;
import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jr;
    private final ShelterRepository sr;

    private final JobApplicationRepository jar;

    @Autowired
    public JobService(JobRepository jr,ShelterRepository sr, JobApplicationRepository jar) {
        this.jr = jr;
        this.sr = sr;
        this.jar = jar;
    }
    // 일자리 등록 로직
    public Job createJob(Integer shelterId, JobType jobType) {
        Shelter shelter = sr.findById(shelterId)
                .orElseThrow(() -> new IllegalArgumentException("쉼터를 찾을 수 없습니다."));
        Job job = new Job();
        job.setShelter(shelter);
        job.setJobType(jobType);
        return jr.save(job);
    }

    public List<JobApplication> getJobApplicationsByJobId(Integer jobId) {
        return jar.findByJob_JobId(jobId);
    }

    public List<JobApplication> getApplicationsByShelterId(Integer shelterId) {
        return jar.findByJob_Shelter_ShelterId(shelterId);
    }
    @Transactional
    public void updateApplicationStatus(Integer applicationId, boolean isAccepted) {
        JobApplication application = jar.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setIsAccepted(isAccepted);
        jar.save(application);
    }

}
