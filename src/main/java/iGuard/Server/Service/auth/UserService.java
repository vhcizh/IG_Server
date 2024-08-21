package iGuard.Server.Service.auth;

import iGuard.Server.Dto.UserRequest;
//import iGuard.Server.Dto.UserResponse;

public interface UserService {

    // 회원가입
    void registerUser(UserRequest user);

    // 회원정보 조회
    //UserResponse getUser();

    // 회원정보 수정
    void updateUser(UserRequest user);

    // 회원탈퇴
    void deleteUser();

}
