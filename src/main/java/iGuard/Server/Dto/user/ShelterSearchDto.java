package iGuard.Server.Dto.user;

import lombok.Data;

@Data
public class ShelterSearchDto {

    private String city;
    private String gu;
    private String shelterName;
    private String facilityType;
    private Boolean isOpenAtNight;
    private Boolean allowsAccommodation;
    private Boolean isOpenOnHolidays;
    private int page;
    private int size;
    private String sortBy;
    private Float longitude;
    private Float latitude;

}