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
        List<ShelterPreference> preferences = userRequest.getPreferences();

        // 유저 선호도 저장
        if (preferences != null && !preferences.isEmpty()) {
            List<UserPreference> userPreferences = preferences.stream()
                    .map(preference -> {
                        UserPreference userPreference = new UserPreference();
                        userPreference.setUser(user);
                        userPreference.setPreference(preference);
                        return userPreference;
                    })
                    .toList();

            userPreferenceRepository.saveAll(userPreferences);
        }
    }

    private void validateDuplicateUser(UserRequest user) {
        userRepository.getById(user.getId())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("중복 아이디입니다.");
                });
        userRepository.findByPhoneNumber(user.getPhone_number())
                .ifPresent((e) -> {
                    throw new RuntimeException("이미 가입된 핸드폰 번호입니다.");
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
    public void updateUser(UserUpdate userUpdate) {
        User user = findAndValidatePassword(userUpdate);
        validateDuplicatePhoneNumber(userUpdate, user);
        updatePassword(userUpdate);
        user.updateInfo(userUpdate);
    }

    private User findAndValidatePassword(UserUpdate userUpdate) {
        String id = userContextProvider.getLoginUserId();
        User user = userRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        validatePassword(user, userUpdate.getOldPassword());
        return user;
    }

    private void validatePassword(User user, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }

    private void validateDuplicatePhoneNumber(UserUpdate userUpdate, User currentUser) {
        userRepository.findByPhoneNumber(userUpdate.getPhone_number())
                .ifPresent(existingUser -> {
                    if (!existingUser.getUserid().equals(currentUser.getUserid()))
                        throw new RuntimeException("이미 가입된 핸드폰 번호입니다.");
                });
    }

    private void updatePassword(UserUpdate userUpdate) {
        if (userUpdate.haveNewPassword()) {
            userUpdate.setNewPassword(passwordEncoder.encode(userUpdate.getNewPassword()));
        }
    }

    @Override
    @Transactional
    public void deleteUser() {
        String id = userContextProvider.getLoginUserId();
        userRepository.getById(id).ifPresentOrElse(
                userRepository::delete,
                () -> {throw new RuntimeException("사용자를 찾을 수 없습니다.");}
        );
    }


}
