package iGuard.Server.service.auth;

import iGuard.Server.Entity.User;
import iGuard.Server.dto.UserRequest;
import iGuard.Server.dto.UserResponse;

public interface UserService {

    // 회원가입
    void registerUser(UserRequest user);

    // 회원정보 조회
    UserResponse getUser();

    // 회원정보 수정
    void updateUser(UserRequest user);

    // 회원탈퇴
    void deleteUser();

}
