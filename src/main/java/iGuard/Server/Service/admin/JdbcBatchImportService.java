package iGuard.Server.Service.admin;

import iGuard.Server.Dto.ShelterCsvRowDto;
import iGuard.Server.Repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private final CsvImportDispatcher dispatcher;

    @Override
    public Map<String, Integer> importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int totalFiles = 0, insertedCount = 0, duplicatedCount = 0;

        CsvImportHandler handler = dispatcher.getHandler(fileType);

        for (MultipartFile file : csvFiles) {

            validateFile(file);

            Map<String, Integer> fileImportResult = handler.importCSV(file);

            totalFiles++;
            insertedCount += fileImportResult.getOrDefault("inserted", 0);
            duplicatedCount += fileImportResult.getOrDefault("duplicated", 0);

        }

        result.put("total", totalFiles);
        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("CSV 파일만 업로드할 수 있습니다.");
        }
    }
}