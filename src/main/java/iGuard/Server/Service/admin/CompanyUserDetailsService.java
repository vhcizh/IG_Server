package iGuard.Server.Service.admin;

import iGuard.Server.Entity.CompanyUser;
import iGuard.Server.Repository.CompanyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CompanyUserDetailsService implements UserDetailsService {

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompanyUser user= companyUserRepository.findByCompanyEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getCompanyEmail(),
                user.getPassword(),
                user.isVerified(),
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}