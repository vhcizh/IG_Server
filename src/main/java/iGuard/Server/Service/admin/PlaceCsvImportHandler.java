package iGuard.Server.Service.admin;

import iGuard.Server.Dto.PlaceCsvRowDto;
import iGuard.Server.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PlaceCsvImportHandler extends AbstractCsvImportHandler<PlaceCsvRowDto> {

    private final JdbcTemplate jdbcTemplate;
    private final PlaceRepository placeRepository;
    private static final String[] PLACE_HEADERS = {"관광지명(소재지역)","위도","경도","주소"};

    @Override
    public String getFileType() {
        return "place";
    }

    @Override
    protected String[] getExpectedHeaders() {
        return PLACE_HEADERS;
    }

    @Override
    protected Set<String> getExistingKeys() {
        return new HashSet<>(jdbcTemplate.query(
                "SELECT CONCAT(name, '|', address) as unique_key FROM place",
                (rs, rowNum) -> rs.getString("unique_key")
        ));
    }

    @Override
    protected String extractUniqueKey(String[] row) {
        return row[0] + "|" + row[3]; // 이름 + 주소
    }

    @Override
    protected PlaceCsvRowDto mapRowToDto(String[] row) {
        return PlaceCsvRowDto.from(row);
    }

    @Override
    protected void batchInsert(List<PlaceCsvRowDto> rows) {
        placeRepository.batchInsertPlaces(rows);
    }

}
