package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import iGuard.Server.Dto.CsvImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class AbstractCsvImportHandler<T> implements CsvImportHandler {

    private static final int BATCH_SIZE = 1000;

    @Override
    public CsvImportResult importCSV(MultipartFile file) throws Exception {
        try (CSVReader reader = createReader(file)) {
            validateHeader(reader);

            Set<String> existingKeys = getExistingKeys();
            int insertedCount = 0, duplicatedCount = 0;
            List<T> batch = new ArrayList<>();

            String[] row;
            while ((row = reader.readNext()) != null) {
                String uniqueKey = extractUniqueKey(row);
                if (existingKeys.contains(uniqueKey)) {
                    duplicatedCount++;
                    continue;
                }

                batch.add(mapRowToDto(row));
                existingKeys.add(uniqueKey);
                insertedCount++;

                if (batch.size() >= BATCH_SIZE) {
                    flushBatch(batch);
                }
            }

            flushBatch(batch);

            return new CsvImportResult(insertedCount, duplicatedCount);
        }
    }

    private void validateHeader(CSVReader reader) throws IOException, CsvValidationException {
        String[] headerLine = reader.readNext(); // 헤더
        if (headerLine == null) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        headerLine[0] = headerLine[0].replace("\uFEFF", "");

        if (!Arrays.equals(headerLine, getExpectedHeaders())) {
            throw new IllegalArgumentException("CSV 헤더가 올바르지 않습니다.");
        }
    }

    private CSVReader createReader(MultipartFile file) throws Exception {
        return new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
    }

    private void flushBatch(List<T> batch) {
        if (!batch.isEmpty()) {
            batchInsert(batch);
            batch.clear();
        }
    }


    // === 아래는 하위 클래스에서 구현해야 함 ===
    protected abstract String[] getExpectedHeaders();
    protected abstract Set<String> getExistingKeys();
    protected abstract String extractUniqueKey(String[] row);
    protected abstract T mapRowToDto(String[] row);
    protected abstract void batchInsert(List<T> rows);
}
