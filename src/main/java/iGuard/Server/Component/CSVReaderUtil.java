package iGuard.Server.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import iGuard.Server.Dto.PlaceDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVReaderUtil {
    public List<PlaceDto> readCsv(Path filePath) throws IOException, CsvValidationException {
        List<PlaceDto> places = new ArrayList<>();

        // FileReader 대신 InputStreamReader를 사용하여 UTF-8로 인코딩
        try (CSVReader reader = new CSVReader(new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))) {
            String[] nextLine;
            // Skip header
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String name = nextLine[0];
                Float latitude = Float.parseFloat(nextLine[1]);
                Float longitude = Float.parseFloat(nextLine[2]);
                String address = nextLine[3];
                places.add(new PlaceDto(name, latitude, longitude, address));
            }
        }
        return places;
    }
}