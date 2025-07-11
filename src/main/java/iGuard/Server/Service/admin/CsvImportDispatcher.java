package iGuard.Server.Service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvImportDispatcher {

    private final List<CsvImportHandler> handlers;

    public CsvImportHandler getHandler(String fileType) {
        return handlers.stream()
                .filter(h -> h.getFileType().equalsIgnoreCase(fileType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 파일 타입입니다: " + fileType));
    }
}
