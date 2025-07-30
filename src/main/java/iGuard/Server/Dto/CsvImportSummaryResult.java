package iGuard.Server.Dto;

import lombok.Data;

@Data
public class CsvImportSummaryResult {
    private final int totalFiles;
    private final CsvImportResult combinedResult;

    public CsvImportSummaryResult(int totalFiles, CsvImportResult combinedResult) {
        this.totalFiles = totalFiles;
        this.combinedResult = combinedResult;
    }

    public String formatSummary() {
        return "총 %d개 파일 중 %s".formatted(totalFiles, combinedResult.formatSummary());
    }

}
