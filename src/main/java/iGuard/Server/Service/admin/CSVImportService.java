package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Repository.PlaceRepository;
import iGuard.Server.Repository.ShelterRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CSVImportService {
    private final ShelterRepository shelterRepository;
    private final PlaceRepository placeRepository;
    private final Logger logger = LoggerFactory.getLogger(CSVImportService.class);

    public Map<String, Integer> importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int totalFiles = 0, insertedCount = 0, duplicatedCount = 0;

        for (MultipartFile file : csvFiles) {
            if (file.isEmpty()) continue;

            try {
                Map<String, Integer> fileImportResult = fileType.equals("shelter")
                        ? importShelterCSV(file)
                        : importPlaceCSV(file);

                totalFiles++;
                insertedCount += fileImportResult.get("inserted");
                duplicatedCount += fileImportResult.get("duplicated");

            } catch (Exception e) {
                logger.error("CSV 파일 import 중 오류 발생: " + file.getOriginalFilename(), e);
            }
        }

        result.put("total", totalFiles);
        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }


    private Map<String, Integer> importShelterCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headers = reader.readNext(); // 헤더 스킵
            String[] row;

            while ((row = reader.readNext()) != null) {
                Shelter shelter = createShelterFromRow(row);

                // 중복 체크 로직 추가 (이름과 주소로 중복 확인)
                if (!shelterRepository.existsByShelterNameAndAddress(shelter.getShelterName(), shelter.getAddress())) {
                    shelterRepository.save(shelter);
                    insertedCount++;
                    logger.info("inserted data : " + insertedCount);
                } else {
                    duplicatedCount++;
                }
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private Map<String, Integer> importPlaceCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headers = reader.readNext(); // 헤더 스킵
            String[] row;

            while ((row = reader.readNext()) != null) {
                Place place = createPlaceFromRow(row);

                // 중복 체크 로직 추가 (이름과 주소로 중복 확인)
                if (!placeRepository.existsByNameAndAddress(
                        place.getName(), place.getAddress())) {
                    placeRepository.save(place);
                    insertedCount++;
                } else {
                    duplicatedCount++;
                }
            }
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private Shelter createShelterFromRow(String[] row) {
        return Shelter.builder()
                .facilityType(row[0])
                .shelterName(row[1])
                .address(row[2])
                .isAvailable(Boolean.parseBoolean(row[3]))
                .area(Double.parseDouble(row[4]))
                .capacity(Integer.parseInt(row[5]))
                .hasFan(Integer.parseInt(row[6]))
                .hasAirConditioner(Integer.parseInt(row[7]))
                .isOpenAtNight("y".equalsIgnoreCase(row[8]))
                .isOpenOnHolidays("y".equalsIgnoreCase(row[9]))
                .allowsAccommodation("y".equalsIgnoreCase(row[9]))
                .notes(row[11])
                .managementAgency(row[12])
                .managementAgencyPhone(row[13])
                .facilityTypeName(row[14])
                .latitude(Float.parseFloat(row[15]))
                .longitude(Float.parseFloat(row[16]))
                .build();
    }

    private Place createPlaceFromRow(String[] row) {
        return Place.builder()
                .name(row[0])
                .latitude(Float.parseFloat(row[1]))
                .longitude(Float.parseFloat(row[2]))
                .address(row[3])
                .build();
    }
}