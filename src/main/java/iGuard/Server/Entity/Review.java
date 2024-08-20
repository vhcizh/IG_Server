package iGuard.Server.Entity;

import iGuard.Server.Enum.ReviewContent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shelterid")
    private Shelter shelter;

    @Enumerated(EnumType.STRING)
    @Column(name = "contents", nullable = false)
    private ReviewContent contents;

    @Column(name = "rating", nullable = false)
    private Integer rating;

}
