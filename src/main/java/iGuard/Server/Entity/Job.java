package iGuard.Server.Entity;

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

    @Column(name = "job_type", length = 50, nullable = false)
    private String jobType;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(mappedBy = "job")
    private List<JobApplication> jobApplications;

}