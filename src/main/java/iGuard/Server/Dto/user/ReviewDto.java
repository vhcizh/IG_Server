package iGuard.Server.Dto.user;

import iGuard.Server.Enum.ShelterPreference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewDto {

    private Integer visitedShelterId;
    private Integer shelterId;
    private Double  rating;
    private List<ShelterPreference> categories;

}