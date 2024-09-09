package iGuard.Server.Entity;

import iGuard.Server.Enum.ShelterPreference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_category")
@Getter
@Setter
public class ReviewCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewCategoryId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ShelterPreference category;
}