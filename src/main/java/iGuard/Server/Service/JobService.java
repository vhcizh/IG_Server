package iGuard.Server.Service;


import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Enum.JobType;
import iGuard.Server.Repository.JobRepository;
import iGuard.Server.Repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    private final JobRepository jr;
    private final ShelterRepository sr;


    @Autowired
    public JobService(JobRepository jr, ShelterRepository sr) {
        this.jr = jr;
        this.sr = sr;
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

}
