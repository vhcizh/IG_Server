package iGuard.Server.Repository;

import iGuard.Server.Entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser,Integer> {//
    CompanyUser findByCompanyUserId(int companyUserid);

    CompanyUser findByCompanyEmail(String companyEmail);

    Optional<CompanyUser> findCompanyUserByCompanyEmail(String companyEmail);

    CompanyUser findByCompanyEmailAndPassword(String companyEmail, String password);



}
