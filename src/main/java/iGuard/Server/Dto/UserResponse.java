package iGuard.Server.Dto;

import iGuard.Server.Entity.User;
import iGuard.Server.Util.AgeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer userId;
    private String id;
    private Integer age;
    private String birthDate;
    private String address;
    private String detailAddress;
    private String phone_number;
    private String email;
    private boolean verified;

    public static UserResponse getFrom(User user) {
        return UserResponse.builder()
                .userId(user.getUserid())
                .id(user.getId())
                .age(AgeUtil.calculateAge(user.getAge()))
                .birthDate(user.getAge().format(DateTimeFormatter.BASIC_ISO_DATE)) // 19980319
                .address(user.getAddress().split(",")[0])
                .detailAddress(
                        user.getAddress().split(",").length == 2
                        ? user.getAddress().split(",")[1].trim()
                        : null)
                .phone_number(user.getPhoneNumber())
                .email(user.getEmail())
                .verified(user.isVerified())
                .build();
    }

}
