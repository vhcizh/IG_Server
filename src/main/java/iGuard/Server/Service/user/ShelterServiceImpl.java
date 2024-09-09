package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.ShelterResponse;
import iGuard.Server.Dto.user.ShelterSearchDto;
import iGuard.Server.Repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository shelterRepository;

    @Override
    public Page<ShelterResponse> getShelters(ShelterSearchDto dto, Pageable pageable) {
        return shelterRepository.search(dto, pageable)
                .map(ShelterResponse::toResponse);
    }

    @Override
    public List<String> getFacilityType() {
        return shelterRepository.findDistinctFacilityTypes();
    }

    @Override
    public List<String> getAllCities() {
        return shelterRepository.findDistinctCities();
    }

    @Override
    public List<String> getGus(String city) {
        return shelterRepository.findDistinctGusByCity(city);
    }

    @Override
    public List<String> getDongs(String city, String gu) {
        String addressPattern = city + "%" + gu + "%"; // 예: "서울%강남구%"
        return shelterRepository.findDongsByCityAndGu(addressPattern);
    }

}
