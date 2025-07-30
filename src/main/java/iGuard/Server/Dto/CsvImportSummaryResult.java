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

}
