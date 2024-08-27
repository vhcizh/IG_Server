package iGuard.Server.Dto;

import iGuard.Server.Entity.User;
import iGuard.Server.Util.AgeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private Integer age;
    private LocalDate birthDate;
    private String address;
    private String phone_number;

    public static UserResponse getFrom(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .age(AgeUtil.calculateAge(user.getAge()))
                .birthDate(user.getAge())
                .address(user.getAddress())
                .phone_number(user.getPhone_number())
                .build();
    }

}
