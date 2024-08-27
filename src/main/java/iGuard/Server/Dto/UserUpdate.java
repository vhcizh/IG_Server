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
public class UserUpdate {

    private String id;


    private String password;

    private LocalDate age;


    private String address;


    private String phone_number;

}
