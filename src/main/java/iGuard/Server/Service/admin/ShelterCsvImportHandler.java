package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import iGuard.Server.Dto.ShelterCsvRowDto;
import iGuard.Server.Repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ShelterCsvImportHandler implements CsvImportHandler {

    private final JdbcTemplate jdbcTemplate;
    private final ShelterRepository shelterRepository;
    private static final int BATCH_SIZE = 1000;
    private static final String[] SHELTER_HEADERS = {"시설유형","쉼터명칭","소재지도로명주소","사용여부","시설면적","이용가능인원수","선풍기보유현황","에어컨보유현황","야간개방","휴일개방","숙박가능여부","특이사항","관리기관","관리기관전화번호","시설유형명","위도","경도"};

    @Override
    public String getFileType() {
        return "shelter";
    }

    @Override
    public Map<String, Integer> importCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;
        List<ShelterCsvRowDto> batch = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headerLine = reader.readNext();
            if (headerLine == null || !Arrays.equals(SanitizeBOM(headerLine), SHELTER_HEADERS)) {
                throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
            }

            Set<String> existingKeys = new HashSet<>(jdbcTemplate.query(
                    "SELECT CONCAT(shelter_name, '|', address) FROM shelter",
                    (rs, rowNum) -> rs.getString(1)
            ));

            String[] row;
            while ((row = reader.readNext()) != null) {
                String key = row[1] + "|" + row[2];
                if (!existingKeys.contains(key)) {
                    batch.add(ShelterCsvRowDto.from(row));
                    existingKeys.add(key);
                    insertedCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        shelterRepository.batchInsertShelters(batch);
                        batch.clear();
                    }
                } else {
                    duplicatedCount++;
                }
            }

            if (!batch.isEmpty()) {
                shelterRepository.batchInsertShelters(batch);
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