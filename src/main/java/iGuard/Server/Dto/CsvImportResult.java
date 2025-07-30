package iGuard.Server.Dto;

import lombok.Data;

@Data
public class CsvImportResult {
    private final int insertedCount;
    private final int duplicatedCount;

    public CsvImportResult(int insertedCount, int duplicatedCount) {
        this.insertedCount = insertedCount;
        this.duplicatedCount = duplicatedCount;
    }

    public CsvImportResult add(CsvImportResult other) {
        return new CsvImportResult(
                this.insertedCount + other.insertedCount,
                this.duplicatedCount + other.duplicatedCount
        );
    }

    public String formatSummary() {
        return "%d개 저장, %d개 중복됨".formatted(insertedCount, duplicatedCount);
    }
}
