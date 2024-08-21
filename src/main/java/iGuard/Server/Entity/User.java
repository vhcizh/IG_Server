package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    @Column(name = "age")
    @Temporal(TemporalType.DATE)
    private LocalDate age;

    @Column(name ="accepted" ) //동의 여부
    private boolean accepted;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone_number", length = 14)
    private String phone_number;

    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "password", length = 50)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<VisitedShelter> visitedShelters;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<RecommendedShelter> recommendedShelters;

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<JobApplication> jobApplications;

}