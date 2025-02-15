package iGuard.Server.Service.user;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserResponse;
import iGuard.Server.Dto.UserUpdate;

public interface UserService {

    // 회원가입
    void registerUser(UserRequest user);

    // 회원정보 조회
    UserResponse getUser();

    // 회원정보 수정
    void updateUser(UserUpdate user);

    // 회원탈퇴
    void deleteUser();

    // 비밀번호 변경하기
    void resetPassword(String userId, String email);
}
