package iGuard.Server.service.auth;

import iGuard.Server.dto.UserRequest;
import iGuard.Server.dto.UserResponse;
import iGuard.Server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registerUser(UserRequest user) {
        userRepository.save(user.toEntity());
    }

    @Override
    public UserResponse getUser() {
        return null;
    }

    @Override
    public void updateUser(UserRequest user) {

    }

    @Override
    public void deleteUser() {

    }
}
