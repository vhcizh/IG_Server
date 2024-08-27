package iGuard.Server.Service;

import iGuard.Server.Dto.PlaceDto;
import iGuard.Server.Entity.Place;
import iGuard.Server.Repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public void savePlaces(List<PlaceDto> placeDtos) {
        List<Place> places = placeDtos.stream()
                .map(dto -> Place.builder()
                        .name(dto.getName())
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .address(dto.getAddress())
                        .build())
                .toList();

        placeRepository.saveAll(places);
    }

    public List<Place> getPlacesNear(float lat, float lon, float range) {
        // 위도와 경도의 범위 계산
        float latMin = lat - range;
        float latMax = lat + range;
        float lonMin = lon - range;
        float lonMax = lon + range;

        // 장소를 데이터베이스에서 조회
        return placeRepository.findByLatitudeBetweenAndLongitudeBetween(latMin, latMax, lonMin, lonMax);
    }
}