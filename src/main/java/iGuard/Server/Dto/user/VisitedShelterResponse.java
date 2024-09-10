package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitedShelterResponse {
    Integer visitedShelterId;
    Shelter shelter;
    String date;
    Boolean reviewed;
}
