package iGuard.Server.Service.admin;

import iGuard.Server.Entity.Shelter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class JdbcBatchImportService implements CSVImportService {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcBatchImportService.class);

    private static final int BATCH_SIZE = 1000;

    private static final String[] SHELTER_HEADERS = {"시설유형","쉼터명칭","소재지도로명주소","사용여부","시설면적","이용가능인원수","선풍기보유현황","에어컨보유현황","야간개방","휴일개방","숙박가능여부","특이사항","관리기관","관리기관전화번호","시설유형명","위도","경도"};
    private static final String[] PLACE_HEADERS = {"관광지명(소재지역)","위도","경도","주소"};

    @Override
    public Map<String, Integer> importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int totalFiles = 0, insertedCount = 0, duplicatedCount = 0;

        for (MultipartFile file : csvFiles) {
            if (file.isEmpty()) continue;

            // 파일 형식 검증
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
                throw new IllegalArgumentException("CSV 파일만 업로드할 수 있습니다.");
            }

            Map<String, Integer> fileImportResult = fileType.equals("shelter")
                    ? importShelterCSV(file)
                    : importPlaceCSV(file);

            totalFiles++;
            insertedCount += fileImportResult.get("inserted");
            duplicatedCount += fileImportResult.get("duplicated");

        }

        result.put("total", totalFiles);
        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }


    private Map<String, Integer> importShelterCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headerLine = reader.readNext(); // 헤더 스킵
            List<String[]> batch = new ArrayList<>();
            String[] row;

            // BOM 제거 후 헤더 비교
            if (headerLine[0].startsWith("\uFEFF")) {
                headerLine[0] = headerLine[0].substring(1); // BOM 제거
            }

            // 헤더 검증
            if (headerLine == null || !Arrays.equals(headerLine, SHELTER_HEADERS)) {
                throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
            }

            // 기존 데이터의 unique key (이름+주소) 조회
            Set<String> existingShelters = new HashSet<>(
                    jdbcTemplate.query(
                            "SELECT CONCAT(shelter_name, '|', address) as unique_key FROM shelter",
                            (rs, rowNum) -> rs.getString("unique_key")
                    )
            );

            while ((row = reader.readNext()) != null) {
                String uniqueKey = row[1] + "|" + row[2]; // 이름 + 주소

                if (!existingShelters.contains(uniqueKey)) {
                    batch.add(row);
                    existingShelters.add(uniqueKey);
                    insertedCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        batchInsertShelters(batch);
                        batch.clear();
                    }
                } else {
                    duplicatedCount++;
                }
            }

            // 남은 데이터 처리
            if (!batch.isEmpty()) {
                batchInsertShelters(batch);
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private void batchInsertShelters(List<String[]> rows) {
        jdbcTemplate.batchUpdate(
                """
                INSERT INTO shelter (
                    facility_type, shelter_name, address, is_available, area, 
                    capacity, has_fan, has_air_conditioner, is_open_at_night, 
                    is_open_on_holidays, allows_accommodation, notes, 
                    management_agency, management_agency_phone, facility_type_name, 
                    latitude, longitude
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                        String[] row = rows.get(i);
                        ps.setString(1, row[0]);  // facility_type
                        ps.setString(2, row[1]);  // shelter_name
                        ps.setString(3, row[2]);  // address
                        ps.setBoolean(4, "y".equalsIgnoreCase(row[3]));  // is_available
                        ps.setDouble(5, Double.parseDouble(row[4]));  // area
                        ps.setInt(6, Integer.parseInt(row[5]));  // capacity
                        ps.setInt(7, Integer.parseInt(row[6]));  // has_fan
                        ps.setInt(8, Integer.parseInt(row[7]));  // has_air_conditioner
                        ps.setBoolean(9, "y".equalsIgnoreCase(row[8]));  // is_open_at_night
                        ps.setBoolean(10, "y".equalsIgnoreCase(row[9]));  // is_open_on_holidays
                        ps.setBoolean(11, "y".equalsIgnoreCase(row[10])); // allows_accommodation
                        ps.setString(12, row[11]); // notes
                        ps.setString(13, row[12]); // management_agency
                        ps.setString(14, row[13]); // management_agency_phone
                        ps.setString(15, row[14]); // facility_type_name
                        ps.setFloat(16, Float.parseFloat(row[15])); // latitude
                        ps.setFloat(17, Float.parseFloat(row[16])); // longitude
                    }

                    @Override
                    public int getBatchSize() {
                        return rows.size();
                    }
                }
            );
        }


    private Map<String, Integer> importPlaceCSV(MultipartFile file) throws Exception {

        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headerLine = reader.readNext(); // 헤더 스킵
            List<String[]> batch = new ArrayList<>();
            String[] row;

            // BOM 제거 후 헤더 비교
            if (headerLine[0].startsWith("\uFEFF")) {
                headerLine[0] = headerLine[0].substring(1); // BOM 제거
            }

            // 헤더 검증
            if (headerLine == null || !Arrays.equals(headerLine, PLACE_HEADERS)) {
                throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
            }

            // 기존 데이터의 unique key (이름+주소) 조회
            Set<String> existingPlaces = new HashSet<>(
                    jdbcTemplate.query(
                            "SELECT CONCAT(name, '|', address) as unique_key FROM place",
                            (rs, rowNum) -> rs.getString("unique_key")
                    )
            );

            while ((row = reader.readNext()) != null) {
                String uniqueKey = row[0] + "|" + row[3]; // 이름 + 주소

                if (!existingPlaces.contains(uniqueKey)) {
                    batch.add(row);
                    existingPlaces.add(uniqueKey);
                    insertedCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        batchInsertPlaces(batch);
                        batch.clear();
                    }
                } else {
                    duplicatedCount++;
                }
            }

            // 남은 데이터 처리
            if (!batch.isEmpty()) {
                batchInsertPlaces(batch);
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private void batchInsertPlaces(List<String[]> rows) {
        jdbcTemplate.batchUpdate(
                """
                INSERT INTO place (
                    address, latitude, longitude, name
                ) VALUES (?, ?, ?, ?)
                """,
                new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                        String[] row = rows.get(i);
                        ps.setString(1, row[3]);  // address
                        ps.setString(2, row[1]);  // latitude
                        ps.setString(3, row[2]);  // longitude
                        ps.setString(4, row[0]);  // name
                    }

                    @Override
                    public int getBatchSize() {
                        return rows.size();
                    }
                }
        );
    }
}