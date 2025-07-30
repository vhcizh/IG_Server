package iGuard.Server.Service.admin;

import com.opencsv.CSVReader;
import iGuard.Server.Dto.CsvImportResult;
import iGuard.Server.Dto.CsvImportSummaryResult;
import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Repository.PlaceRepository;
import iGuard.Server.Repository.ShelterRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HibernateBatchImportService implements CSVImportService {
    private final ShelterRepository shelterRepository;
    private final PlaceRepository placeRepository;
    private final Logger logger = LoggerFactory.getLogger(HibernateBatchImportService.class);
    private final EntityManager entityManager;

    @Override
    public CsvImportSummaryResult importCSVFiles(List<MultipartFile> csvFiles, String fileType) throws Exception {
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

        return new CsvImportSummaryResult(totalFiles, new CsvImportResult(insertedCount, duplicatedCount));
    }


    private Map<String, Integer> importShelterCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;
        List<Shelter> sheltersToSave = new ArrayList<>();


        // 기존 데이터 조회 및 캐싱
        Set<String> existingShelters = shelterRepository
                .findAll()
                .stream()
                .map(shelter -> shelter.getShelterName() + "|" + shelter.getAddress())
                .collect(Collectors.toSet());

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headers = reader.readNext(); // 헤더 스킵
            String[] row;

            while ((row = reader.readNext()) != null) {
                String uniqueKey = row[1] + "|" + row[2]; // 이름 + 주소

                if (!existingShelters.contains(uniqueKey)) {
                    existingShelters.add(uniqueKey);
                    sheltersToSave.add(createShelterFromRow(row));
                    insertedCount++;
//                    logger.info("inserted data : " + insertedCount);
                } else {
                    duplicatedCount++;
                }
            }
            // 한 번에 저장
//            shelterRepository.saveAll(sheltersToSave);
            saveBatch(sheltersToSave);
            logger.info(file.getOriginalFilename() + " 파일 저장 완료");
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private Map<String, Integer> importPlaceCSV(MultipartFile file) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int insertedCount = 0, duplicatedCount = 0;
        List<Place> placesToSave = new ArrayList<>();

        // 기존 데이터 조회 및 캐싱
        Set<String> existingPlaces = placeRepository
                .findAll()
                .stream()
                .map(place -> place.getName() + "|" + place.getAddress())
                .collect(Collectors.toSet());

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] headers = reader.readNext(); // 헤더 스킵
            String[] row;

            while ((row = reader.readNext()) != null) {
                String uniqueKey = row[0] + "|" + row[3]; // 이름 + 주소

                if (!existingPlaces.contains(uniqueKey)) {
                    existingPlaces.add(uniqueKey);
                    placesToSave.add(createPlaceFromRow(row));
                    insertedCount++;
//                    logger.info("inserted data : " + insertedCount);
                } else {
                    duplicatedCount++;
                }
            }
            // 한 번에 저장
//            placeRepository.saveAll(placesToSave);
            saveBatch(placesToSave);
            logger.info(file.getOriginalFilename() + " 파일 저장 완료");
        }

        result.put("inserted", insertedCount);
        result.put("duplicated", duplicatedCount);
        return result;
    }

    private void saveBatch(List<?> list) {
        int batchSize = 100;
        for (int i = 0; i < list.size(); i++) {
            entityManager.persist(list.get(i));

            // 배치 크기마다 flush 및 clear 호출
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // 남아있는 데이터 flush
        entityManager.flush();
        entityManager.clear();
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