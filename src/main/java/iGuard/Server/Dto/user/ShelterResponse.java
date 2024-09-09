package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.Data;

@Data
public class ShelterResponse {

    private Integer shelterId;
    private String shelterName;
    private String capacity;
    private Integer distance;

    public static ShelterResponse toResponse(Shelter shelter) {
        ShelterResponse response = new ShelterResponse();
        response.setShelterId(shelter.getShelterId());
        response.setShelterName(shelter.getShelterName());
        response.setCapacity(shelter.getCurrentOccupancy() + " / " + shelter.getCapacity());
//        response.setDistance();
        return response;
    }

}
