package iGuard.Server.Dto.user;

import lombok.Data;

@Data
public class ShelterSearchDto {

    private String city;
    private String gu;
    private String shelterName;
    private String facilityType;
    private Boolean isOpenAtNight = true;
    private Boolean allowsAccommodation = true;
    private Boolean isOpenOnHolidays = true;
    private int page;
    private int size;
    private String sortBy = "name";

}