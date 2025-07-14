package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class AbstractCsvImportHandler<T> implements CsvImportHandler {

    private static final int BATCH_SIZE = 1000;

    @Override
    public Map<String, Integer> importCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;
        List<T> batch = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headerLine = reader.readNext(); // 헤더
            if (headerLine == null || !Arrays.equals(stripBOM(headerLine), getExpectedHeaders())) {
                throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
            }

            Set<String> existingKeys = getExistingKeys();

            String[] row;
            while ((row = reader.readNext()) != null) {
                String uniqueKey = extractUniqueKey(row);
                if (!existingKeys.contains(uniqueKey)) {
                    batch.add(mapRowToDto(row));
                    existingKeys.add(uniqueKey);
                    insertedCount++;

                    if (batch.size() >= BATCH_SIZE) {
                        batchInsert(batch);
                        batch.clear();
                    }
                } else {
                    duplicatedCount++;
                }
            }

            if (!batch.isEmpty()) {
                batchInsert(batch);
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private String[] stripBOM(String[] headers) {
        headers[0] = headers[0].replace("\uFEFF", "");
        return headers;
    }

    // === 아래는 하위 클래스에서 구현해야 함 ===
    protected abstract String[] getExpectedHeaders();
    protected abstract Set<String> getExistingKeys();
    protected abstract String extractUniqueKey(String[] row);
    protected abstract T mapRowToDto(String[] row);
    protected abstract void batchInsert(List<T> rows);
}
