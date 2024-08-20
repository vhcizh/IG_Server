package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "job_application")
@Getter
@Setter
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jobid")
    private Job job;

    @Column(name = "application_text", columnDefinition = "TEXT")
    private String applicationText;

}
