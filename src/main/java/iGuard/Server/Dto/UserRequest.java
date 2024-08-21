package iGuard.Server.Dto;

import iGuard.Server.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String id;


    private String password;


    private LocalDate age;


    private String address;


    private String phone_number;

    private boolean accepted;

    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setAge(age);
        user.setAddress(address);
        user.setPhone_number(phone_number);
        user.setAccepted(accepted);
        return user;
    }

}