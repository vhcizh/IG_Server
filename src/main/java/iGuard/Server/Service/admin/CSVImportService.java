package iGuard.Server.Service.admin;

import iGuard.Server.Dto.CsvImportSummaryResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CSVImportService {

    CsvImportSummaryResult importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception;

}
