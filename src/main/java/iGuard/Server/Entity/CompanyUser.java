package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "company_user")
@Getter
@Setter
public class CompanyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer corporateUserId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_email", length = 50)
    private String companyEmail;

    @OneToMany(mappedBy = "corporateUser")
    private List<InitialRecommendation> initialRecommendations;

    @OneToMany(mappedBy = "corporateUser")
    private List<FinalRecommendation> finalRecommendations;

}