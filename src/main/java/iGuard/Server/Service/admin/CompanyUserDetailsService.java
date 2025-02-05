package iGuard.Server.Service.admin;

import iGuard.Server.Repository.CompanyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyUserDetailsService implements UserDetailsService {

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return companyUserRepository.findCompanyUserByCompanyEmail(username)
                .map(member -> new User(
                        member.getCompanyEmail(),
                        member.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));
    }
}