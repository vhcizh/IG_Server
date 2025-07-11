package iGuard.Server.Service.admin;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CsvImportHandler {

    String getFileType();

    Map<String, Integer> importCSV(MultipartFile file) throws Exception;

}
