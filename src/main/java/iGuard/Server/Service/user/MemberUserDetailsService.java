package iGuard.Server.Service.user;

import iGuard.Server.Entity.User;
import iGuard.Server.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER")));
    }
}