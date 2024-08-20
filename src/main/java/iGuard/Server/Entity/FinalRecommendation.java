package iGuard.Server.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "final_recommendation")
public class FinalRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer finalRecommendationId;

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
