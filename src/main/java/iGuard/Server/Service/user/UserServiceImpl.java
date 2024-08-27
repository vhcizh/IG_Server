package iGuard.Server.Service.user;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserResponse;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Util.SecurityUserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUserContextProvider userContextProvider;

    @Override
    @Transactional
    public void registerUser(UserRequest user) {
        validateDuplicateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user.toEntity());
    }

    private void validateDuplicateUser(UserRequest user) {
        userRepository.getById(user.getId())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("already existing user");
                });
    }

    @Override
    public UserResponse getUser() {
        String id = userContextProvider.getLoginUserId();
        return userRepository.getById(id)
                .map(UserResponse::getFrom)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public void updateUser(UserUpdate user) {
        String id = userContextProvider.getLoginUserId();
        userRepository.getById(id).ifPresent(
                u -> u.updateInfo(user)
        );
    }

    @Override
    public void deleteUser() {
        String id = userContextProvider.getLoginUserId();
        userRepository.getById(id).ifPresent(
                userRepository::delete
        );
    }
}
