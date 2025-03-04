package iGuard.Server.Service.user;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserResponse;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Entity.User;
import iGuard.Server.Entity.UserPreference;
import iGuard.Server.Enum.ShelterPreference;
import iGuard.Server.Repository.UserPreferenceRepository;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Service.EmailService;
import iGuard.Server.Util.SecurityUserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUserContextProvider userContextProvider;
    private final UserPreferenceRepository userPreferenceRepository;
    private final EmailService emailService;

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
        userRepository.findByEmail(user.getEmail())
                .ifPresent((e) -> {
                    throw new RuntimeException(("이미 가입된 이메일입니다."));
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

    @Override
    @Transactional
    public void resetPassword(String userId, String email) {
        User user = userRepository.findByIdAndEmail(userId, email)
                .orElseThrow(() -> new RuntimeException("일치하는 계정을 찾을 수 없습니다"));

        if(!user.isVerified()) {
            throw new RuntimeException("인증되지 않은 이메일은 임시 비밀번호 발급이 불가능합니다.");
        }

        // 임시 비밀번호 생성 (영문 대소문자, 숫자, 특수문자 포함 12자리)
        String tempPassword = generateTempPassword();

        // 임시 비밀번호로 업데이트
        user.setPassword(passwordEncoder.encode(tempPassword));

        // 이메일 발송
        emailService.sendTempPasswordEmail(email, tempPassword);
    }

    private String generateTempPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new SecureRandom(); // 보안적으로 안전한 난수 생성기

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";

        // 각 문자 종류별로 최소 1개씩 포함
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        // 나머지 8자리는 모든 문자들 중에서 랜덤 선택
        String allChars = upper + lower + digits + special;
        for (int i = 0; i < 8; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 생성된 비밀번호를 섞어서 패턴 방지
        char[] tempPass = password.toString().toCharArray();
        for (int i = tempPass.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = tempPass[i];
            tempPass[i] = tempPass[j];
            tempPass[j] = temp;
        }

        return new String(tempPass);
    }

    @Override
    @Transactional
    public boolean isVerifiedEmail() {
        String id = userContextProvider.getLoginUserId();
        User user = userRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.isVerified();
    };

    @Override
    @Transactional
    public void verifyEmail(String email) {
        String id = userContextProvider.getLoginUserId();
        userRepository.findByIdAndEmail(id, email)
                .ifPresentOrElse(
                        user->user.setVerified(true),
                        () -> {throw new RuntimeException("사용자를 찾을 수 없습니다.");}
                );
    }

}
