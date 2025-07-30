package iGuard.Server.Service.admin;

import iGuard.Server.Dto.CsvImportResult;
import org.springframework.web.multipart.MultipartFile;

public interface CsvImportHandler {

    String getFileType();

    CsvImportResult importCSV(MultipartFile file) throws Exception;

}
