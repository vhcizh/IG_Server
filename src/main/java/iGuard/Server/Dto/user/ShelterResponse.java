package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.Data;

@Data
public class ShelterResponse {

    private Integer shelterId;
    private String shelterName;
    private String capacity;
    private Integer distance;
    private String address;
    private Integer fanCount;
    private Integer airConditionerCount;
    private Float latitude;
    private Float longitude;

    public static ShelterResponse toResponse(Shelter shelter) {
        ShelterResponse response = new ShelterResponse();
        response.setShelterId(shelter.getShelterId());
        response.setShelterName(shelter.getShelterName());
        response.setCapacity(shelter.getCurrentOccupancy() + " / " + shelter.getCapacity());
//        response.setDistance();
        response.setAddress(shelter.getAddress());
        response.setFanCount(shelter.getHasFan());
        response.setAirConditionerCount(shelter.getHasAirConditioner());
        response.setLatitude(shelter.getLatitude());
        response.setLongitude(shelter.getLongitude());
        return response;
    }

}
