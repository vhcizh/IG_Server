package iGuard.Server;

import com.opencsv.exceptions.CsvException;
import iGuard.Server.Service.CSVService;
import iGuard.Server.Service.admin.CSVImportService;
import iGuard.Server.Service.admin.HibernateBatchImportService;
import iGuard.Server.Service.admin.JdbcBatchImportService;
import iGuard.Server.Service.admin.OldCSVImportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional // 자동 롤백
class CompareCSVImportServicesTest {

    final String shelterFilePath = "src/main/resources/shelter.csv";
    final MultipartFile multipartFile;

    @Autowired
    CSVService csvService;

    @Autowired
    OldCSVImportService oldCsvImportService;

    @Autowired
    HibernateBatchImportService hibernateBatchImportService;

    @Autowired
    JdbcBatchImportService jdbcBatchImportService;

    CompareCSVImportServicesTest() throws IOException {
        this.multipartFile = convertCSVFileToMultipartFile();
    }

    MultipartFile convertCSVFileToMultipartFile() throws IOException {
        File file = new File(shelterFilePath);
        FileInputStream input = new FileInputStream(file);

        return new MockMultipartFile(
                "shelter",
                file.getName(),
                "text/csv",
                input
        );
    }

    @Test
    @DisplayName("첫번째 버전 테스트 - 기존 팀원이 진행했던 방식")
    void version1() throws IOException, CsvException {
        csvService.importCSV(shelterFilePath);
    }

    @Test
    @DisplayName("두번째 버전 테스트 - 중복검사 기능 추가로 인한 성능 저하")
    void version2() throws Exception {
        oldCsvImportService.importCSVFiles(List.of(multipartFile),"shelter");

    }

    @Test
    @DisplayName("세번째 버전 테스트 - 하이버네이트 배치 처리")
    void version3() throws Exception {
        hibernateBatchImportService.importCSVFiles(List.of(multipartFile), "shelter");
    }

    @Test
    @DisplayName("네번째 버전 테스트 - Jdbc Batch 처리")
    void version4() throws Exception {
        jdbcBatchImportService.importCSVFiles(List.of(multipartFile), "shelter");
    }

}
