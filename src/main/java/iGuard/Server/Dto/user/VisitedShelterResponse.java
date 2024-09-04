package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VisitedShelterResponse {
    Shelter shelter;
    String date;
}
