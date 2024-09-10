package iGuard.Server.Entity;

import iGuard.Server.Enum.ShelterPreference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_preference")
@Getter
@Setter
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer preferenceId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference", nullable = false)
    private ShelterPreference preference;

}