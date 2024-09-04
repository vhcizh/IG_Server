package iGuard.Server;

import com.opencsv.exceptions.CsvValidationException;
import iGuard.Server.Component.CSVReaderUtil;
import iGuard.Server.Dto.PlaceDto;
import iGuard.Server.Service.CSVService;
import iGuard.Server.Service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class IGuardServerApplication{
	//implements CommandLineRunner
	@Autowired
	private CSVReaderUtil csvReaderUtil;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private CSVService csvService;

	public static void main(String[] args)  {
		SpringApplication.run(IGuardServerApplication.class, args);
	}




	/*
	@Override
	public void run(String... args) throws Exception {
		csvService.importCSV1("src/main/resources/shade.csv");
		csvService.importCSV2("src/main/resources/toilet.csv");
	}

	 */



	/*
	@Override
	public void run(String... args) throws Exception {
		try {
			// CSV 파일이 있는 디렉토리
			String directoryPath = "data";
			File folder = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(directoryPath)).getFile());
			File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

			if (files != null) {
				List<PlaceDto> allPlaceDtos = new ArrayList<>();

				for (File file : files) {
					// 파일의 절대 경로를 Path 객체로 변환하여 전달
					List<PlaceDto> placeDtos = csvReaderUtil.readCsv(file.toPath());
					allPlaceDtos.addAll(placeDtos);
				}

				// 모든 CSV 파일의 데이터를 저장
				placeService.savePlaces(allPlaceDtos);
			}
		} catch (IOException | CsvValidationException e) {
			e.printStackTrace();
		}
	}
	 */

}