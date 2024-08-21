package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "company_user")
@NoArgsConstructor
@Getter
@Setter
public class CompanyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "companyuserid")
    private Integer companyUserId;

    @Column(name = "companyname", nullable = false)
    private String companyName;

    @Column(name = "companyemail", length = 50)
    private String companyEmail;

    @Column(name = "password", length =255)
    private String password;
    
    @Column(name = "verified")
    private boolean verified; // 메일 인증 여부

    @OneToMany(mappedBy = "corporateUser")
    private List<InitialRecommendation> initialRecommendations;

    @OneToMany(mappedBy = "corporateUser")
    private List<FinalRecommendation> finalRecommendations;

}