package iGuard.Server.Service.auth;

import iGuard.Server.Entity.CompanyUser;
import iGuard.Server.Repository.CompanyUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public CompanyUser findByCompanyEmail(String email) {
        return userRepository.findByCompanyEmail(email);
    }

    public boolean checkPassword(CompanyUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public void registerUser(CompanyUser user) {
        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
