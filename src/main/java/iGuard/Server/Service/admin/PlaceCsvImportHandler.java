package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import iGuard.Server.Dto.PlaceCsvRowDto;
import iGuard.Server.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PlaceCsvImportHandler implements CsvImportHandler {

    private final JdbcTemplate jdbcTemplate;
    private final PlaceRepository placeRepository;
    private static final int BATCH_SIZE = 1000;
    private static final String[] PLACE_HEADERS = {"관광지명(소재지역)","위도","경도","주소"};

    @Override
    public String getFileType() {
        return "place";
    }

    @Override
    public Map<String, Integer> importCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;
        List<PlaceCsvRowDto> batch = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headerLine = reader.readNext(); // 헤더 스킵

            // 헤더 검증
            if (headerLine == null || !Arrays.equals(SanitizeBOM(headerLine), PLACE_HEADERS)) {
                throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
            }

            // 기존 데이터의 unique key (이름+주소) 조회
            Set<String> existingPlaces = new HashSet<>(
                    jdbcTemplate.query(
                            "SELECT CONCAT(name, '|', address) as unique_key FROM place",
                            (rs, rowNum) -> rs.getString("unique_key")
                    )
            );

            String[] row;
            while ((row = reader.readNext()) != null) {
                String uniqueKey = row[0] + "|" + row[3]; // 이름 + 주소

                if (!existingPlaces.contains(uniqueKey)) {
                    batch.add(PlaceCsvRowDto.from(row));
                    existingPlaces.add(uniqueKey);
                    insertedCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        placeRepository.batchInsertPlaces(batch);
                        batch.clear();
                    }
                } else {
                    duplicatedCount++;
                }
            }

            // 남은 데이터 처리
            if (!batch.isEmpty()) {
                placeRepository.batchInsertPlaces(batch);
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private String[] SanitizeBOM(String[] header) {
        if (header[0].startsWith("\uFEFF")) {
            header[0] = header[0].substring(1);
        }
        return header;
    }
}
