package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShelterDistanceDto {

    private Shelter shelter;
    private Float distance;

}
