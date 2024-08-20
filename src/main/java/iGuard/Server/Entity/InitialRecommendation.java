package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "initial_recommendation")
@Getter
@Setter
public class InitialRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer initialRecommendationId;

    @ManyToOne
    @JoinColumn(name = "corporate_user_id")
    private CompanyUser corporateUser;

    @Column(name = "recommendation_score")
    private Float recommendationScore;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

}