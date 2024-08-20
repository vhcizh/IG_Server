package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recommended_shelter")
@Getter
@Setter
public class RecommendedShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shelterid")
    private Shelter shelter;

    @Column(name = "recommendation_score")
    private Float recommendationScore;

}