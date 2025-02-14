package iGuard.Server.Dto;

import iGuard.Server.Enum.ShelterPreference;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewCategoryCount {

    ShelterPreference shelterPreference;
    Long count;

}
