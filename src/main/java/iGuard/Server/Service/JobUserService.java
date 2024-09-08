package iGuard.Server.Service;

import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.JobApplication;
import iGuard.Server.Entity.User;
import iGuard.Server.Repository.JobApplicationRepository;
import iGuard.Server.Repository.JobRepository;
import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobUserService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ShelterRepository sr;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public JobUserService(JobRepository jr, ShelterRepository sr, UserRepository ur,JobApplicationRepository jar) {
        this.jobRepository = jr;
        this.userRepository = ur;
        this.sr = sr;
        this.jobApplicationRepository = jar;
    }

    public List<Job> getJobsNearUser(float lat, float lon) {
        // JobRepository에서 사용자의 위치 근처의 일자리 가져오기
        float distance = 2.0f;
        return jobRepository.findJobsNearLocation(lat, lon,distance);
    }

    public List<Job> getJobsByShelter(Integer shelterId) {
        return jobRepository.findByShelterShelterId(shelterId);
    }


    public void applyForJob(Integer jobId, Integer userId, String applicationText) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);
        jobApplication.setApplicationText(applicationText);

        jobApplicationRepository.save(jobApplication);
    }
}
