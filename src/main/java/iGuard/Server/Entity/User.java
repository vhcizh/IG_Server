package iGuard.Server.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iGuard.Server.Dto.UserUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "common_user")
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

    @Column(name ="accepted", nullable = false) //동의 여부
    private boolean accepted;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone_number", length = 14, unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "id", length = 50, unique = true, nullable = false)
    private String id;

    @Column(name = "password", length = 60)
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
    @JsonIgnore
    private List<JobApplication> jobApplications;

    @Column(name = "fcm_token", length = 255)
    private String fcmToken;

    @Column(name = "job_id", nullable = true)
    private Integer jobId; // 합격된 일자리 ID

    public void setJob(Job job) {
        if (job != null) {
            this.jobId = job.getJobId();
        } else {
            this.jobId = null;
        }
    }
    public void updateInfo(UserUpdate userRequest) {
        this.setPassword(userRequest.getPassword());
        this.setAge(userRequest.getAge());
        this.setAddress(
                userRequest.getDetailAddress()!=null
                        ? userRequest.getAddress() + ", " + userRequest.getDetailAddress()
                        : userRequest.getAddress()
        );
        this.setPhoneNumber(userRequest.getPhone_number());
    }




}