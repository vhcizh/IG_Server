package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Shelter;
import lombok.Data;

@Data
public class ShelterResponse {

    private Integer shelterId;
    private String shelterName;
    private String capacity;
    private String distance;
    private String address;
    private Integer fanCount;
    private Integer airConditionerCount;
    private Float latitude;
    private Float longitude;

    public static ShelterResponse toResponse(Shelter shelter) {
        ShelterResponse response = new ShelterResponse();
        response.setShelterId(shelter.getShelterId());
        response.setShelterName(shelter.getShelterName());
        response.setCapacity(shelter.getCapacity()+"명");
        response.setAddress(shelter.getAddress());
        response.setFanCount(shelter.getHasFan());
        response.setAirConditionerCount(shelter.getHasAirConditioner());
        response.setLatitude(shelter.getLatitude());
        response.setLongitude(shelter.getLongitude());
        return response;
    }

    public static ShelterResponse toResponse(ShelterDistanceDto dto) {
        ShelterResponse response = new ShelterResponse();
        Shelter shelter = dto.getShelter();
        response.setShelterId(shelter.getShelterId());
        response.setShelterName(shelter.getShelterName());
        response.setCapacity(shelter.getCurrentOccupancy() + " / " + shelter.getCapacity());
        response.setDistance(formatDistance(dto.getDistance()));
        response.setAddress(shelter.getAddress());
        response.setFanCount(shelter.getHasFan());
        response.setAirConditionerCount(shelter.getHasAirConditioner());
        response.setLatitude(shelter.getLatitude());
        response.setLongitude(shelter.getLongitude());
        return response;
    }

    private static String formatDistance(Float distanceInMeters) {
        if (distanceInMeters == null) return "-";
        if (distanceInMeters < 1000) {
            return Math.round(distanceInMeters) + "m"; // 반올림
        } else {
            return String.format("%.2fkm", distanceInMeters / 1000); // 1km 단위 변환
        }
    }

}
