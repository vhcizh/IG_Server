package iGuard.Server.Service.admin;

import iGuard.Server.Dto.CsvImportResult;
import iGuard.Server.Dto.CsvImportSummaryResult;
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
    public CsvImportSummaryResult importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception {
        int totalFiles = 0;
        CsvImportResult totalResult = new CsvImportResult(0, 0);

        CsvImportHandler handler = dispatcher.getHandler(fileType);

        for (MultipartFile file : csvFiles) {

            validateFile(file);

            CsvImportResult fileImportResult = handler.importCSV(file);

            totalFiles++;
            totalResult = totalResult.add(fileImportResult);

        }

        return new CsvImportSummaryResult(totalFiles, totalResult);
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