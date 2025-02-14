package iGuard.Server.Dto.user;

import iGuard.Server.Enum.ShelterPreference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequest {

    private Integer visitedShelterId;
    private Integer shelterId;
    private Double  rating = 0.5;
    private List<ShelterPreference> categories;

}