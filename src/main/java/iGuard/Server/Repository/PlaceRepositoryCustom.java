package iGuard.Server.Repository;

import iGuard.Server.Dto.PlaceCsvRowDto;

import java.util.List;

public interface PlaceRepositoryCustom {

    // 주변 명소 batch insert
    void batchInsertPlaces(List<PlaceCsvRowDto> rows);

}
