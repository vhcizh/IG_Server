package iGuard.Server.Service;

import com.opencsv.CSVReader;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvException;

@Service
public class CSVService {

    @Autowired
    private ShelterRepository shelterRepository;

    public void importCSV(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Assuming the first row is the header

            for (String[] row : rows) {
                Shelter shelter = new Shelter();
                shelter.setFacilityType(row[0]);
                shelter.setShelterName(row[1]);
                shelter.setAddress(row[2]);
                shelter.setIsAvailable(Boolean.parseBoolean(row[3]));
                shelter.setArea(Double.parseDouble(row[4]));
                shelter.setCapacity(Integer.parseInt(row[5]));
                shelter.setHasFan(Integer.parseInt(row[6]));
                shelter.setHasAirConditioner(Integer.parseInt(row[7]));
                shelter.setIsOpenAtNight("y".equalsIgnoreCase(row[8]));
                shelter.setIsOpenOnHolidays("y".equalsIgnoreCase(row[9]));
                shelter.setAllowsAccommodation("y".equalsIgnoreCase(row[9]));
                shelter.setNotes(row[11]);
                shelter.setManagementAgency(row[12]);
                shelter.setManagementAgencyPhone(row[13]);
                shelter.setFacilityTypeName(row[14]);
                shelter.setLatitude(Float.parseFloat(row[15]));
                shelter.setLongitude(Float.parseFloat(row[16]));

                shelterRepository.save(shelter);
            }
        }
    }
}
