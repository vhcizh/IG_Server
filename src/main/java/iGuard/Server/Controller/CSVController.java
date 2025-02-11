package iGuard.Server.Controller;

import iGuard.Server.Service.admin.CSVImportService;
import iGuard.Server.Service.admin.HibernateBatchImportService;
import iGuard.Server.Service.admin.JdbcBatchImportService;
import iGuard.Server.Service.admin.OldCSVImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CSVController {

//    private final CSVImportService csvImportService;
//    private final OldCSVImportService csvImportService;
//    private final HibernateBatchImportService csvImportService;
    private final JdbcBatchImportService csvImportService;

    @PostMapping("/admin/csv-upload")
    public String uploadCSV(
            @RequestParam("fileType") String fileType,
            @RequestParam("csvFiles") List<MultipartFile> csvFiles,
            RedirectAttributes redirectAttributes
    ) {
        try {

            Map<String, Integer> importResult = csvImportService.importCSVFiles(csvFiles, fileType);

            redirectAttributes.addFlashAttribute("successMessage",
                    "총 " + importResult.get("total") + "개 중 " +
                                        importResult.get("inserted") + "개 저장, " +
                                        importResult.get("duplicated") + "개 중복 스킵");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/admin/mypage";
    }
}