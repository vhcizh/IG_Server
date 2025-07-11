package iGuard.Server.Repository;

import iGuard.Server.Dto.PlaceCsvRowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_PLACE_SQL = """
                INSERT INTO place (
                    address, latitude, longitude, name
                ) VALUES (?, ?, ?, ?)
      """;

    @Override
    public void batchInsertPlaces(List<PlaceCsvRowDto> rows) {
        jdbcTemplate.batchUpdate(
                INSERT_PLACE_SQL,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                        PlaceCsvRowDto dto = rows.get(i);
                        ps.setString(1, dto.address());  // address
                        ps.setFloat(2, dto.latitude());  // latitude
                        ps.setFloat(3, dto.longitude());  // longitude
                        ps.setString(4, dto.name());  // name
                    }

                    @Override
                    public int getBatchSize() {
                        return rows.size();
                    }
                }
        );
    }
}
