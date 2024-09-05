package iGuard.Server.Entity;

import iGuard.Server.Enum.JobType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "job")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", length = 50, nullable = false)
    private JobType jobType;


    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(mappedBy = "job")
    private List<JobApplication> jobApplications;

}