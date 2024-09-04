package iGuard.Server.Service.user;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserResponse;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Entity.User;
import iGuard.Server.Entity.UserPreference;
import iGuard.Server.Enum.ShelterPreference;
import iGuard.Server.Repository.UserPreferenceRepository;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Util.SecurityUserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUserContextProvider userContextProvider;
    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    @Transactional
    public void registerUser(UserRequest userRequest) {
        validateDuplicateUser(userRequest);
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userRepository.save(userRequest.toEntity());

        // 유저 선호도 저장
        if(userRequest.getPreferences() != null && !userRequest.getPreferences().isEmpty()) {
            List<UserPreference> userPreferences = new ArrayList<>();
            for (ShelterPreference preference : userRequest.getPreferences()) {
                if (preference.isAvailableForSignup()) {
                    UserPreference userPreference = new UserPreference();
                    userPreference.setUser(user);
                    userPreference.setPreference(preference);
                    userPreferences.add(userPreference);
                }
            }
            userPreferenceRepository.saveAll(userPreferences);
        }
    }

    private void validateDuplicateUser(UserRequest user) {
        userRepository.getById(user.getId())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("already existing user");
                });
    }

    @Override
    @Transactional
    public UserResponse getUser() {
        String id = userContextProvider.getLoginUserId();
        return userRepository.getById(id)
                .map(UserResponse::getFrom)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void updateUser(UserUpdate user) {
        String id = userContextProvider.getLoginUserId();
        userRepository.getById(id).ifPresent(
                u -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    u.updateInfo(user);
                }
        );
    }

    @Override
    @Transactional
    public void deleteUser() {
        String id = userContextProvider.getLoginUserId();
        userRepository.getById(id).ifPresent(
                userRepository::delete
        );
    }
}
