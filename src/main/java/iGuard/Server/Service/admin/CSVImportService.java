package iGuard.Server.Service.admin;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CSVImportService {

    Map<String, Integer> importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception;


}
