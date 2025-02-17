package iGuard.Server.Dto;

import iGuard.Server.Entity.User;
import iGuard.Server.Enum.ShelterPreference;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Pattern(regexp = "\\d{8}", message = "생년월일 형식이 올바르지 않습니다.")
    private String birthday;

    private String address;

    private String detailAddress;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.") // 예: 010-1234-5678
    private String phone_number;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private List<ShelterPreference> preferences; // 카테고리 리스트 추가

    @AssertTrue(message = "약관에 동의해야 합니다.")
    private boolean accepted;

    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setAge(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyyMMdd")));
        user.setAddress(
                detailAddress==null || detailAddress.isBlank()
                        ?address
                        :address + ", " + detailAddress
        );
        user.setPhoneNumber(phone_number);
        user.setEmail(email);
        user.setAccepted(accepted);
        user.setVerified(false);
        return user;
    }

}