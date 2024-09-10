package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.ShelterResponse;
import iGuard.Server.Dto.user.ShelterSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ShelterService {

    // 전국 쉼터 리스트 조회
    Page<ShelterResponse> getShelters(ShelterSearchDto dto, Pageable pageable);

    // 쉼터 유형 조회
    List<String> getFacilityType();

    // 시 조회
    List<String> getAllCities();

    // 구 조회
    List<String> getGus(String city);

    // 동 조회
    List<String> getDongs(String city, String gu);

    // 쉼터 상세 조회
    ShelterResponse getShelterById(Integer shelterId);
}
