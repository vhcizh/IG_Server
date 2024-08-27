package iGuard.Server.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlaceDto {
    private String name;
    private Float latitude;
    private Float longitude;
    private String address;
}