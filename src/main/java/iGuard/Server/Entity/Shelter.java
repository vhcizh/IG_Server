package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shelter")
@Getter
@Setter
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shelterId;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy;

    @OneToMany(mappedBy = "shelter")
    private List<VisitedShelter> visitedShelters;

    @OneToMany(mappedBy = "shelter")
    private List<RecommendedShelter> recommendedShelters;

    @OneToMany(mappedBy = "shelter")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "shelter")
    private List<Review> reviews;

    @OneToMany(mappedBy = "shelter")
    private List<Job> jobs;

}