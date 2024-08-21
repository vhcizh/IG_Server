package iGuard.Server.Util;

import java.time.LocalDate;
import java.time.Period;

public class AgeUtil {

    // 만 나이 계산기
    public static int calculateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        Period age = Period.between(birthDate, today);
        return age.getYears();
    }

}