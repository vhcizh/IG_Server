package iGuard.Server.Service.user;

import iGuard.Server.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

        return userRepository.getById(username)
                .map(member -> new User(
                        member.getId(),
                        member.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + username));
    }
}