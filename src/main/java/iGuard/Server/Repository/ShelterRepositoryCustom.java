package iGuard.Server.Repository;

import iGuard.Server.Dto.ShelterCsvRowDto;
import iGuard.Server.Dto.user.ShelterDistanceDto;
import iGuard.Server.Dto.user.ShelterSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShelterRepositoryCustom {

    // 쉼터 조회 (필터링 동적 조회)
    Page<ShelterDistanceDto> search(ShelterSearchDto searchDto, Pageable pageable);

    // 쉼터 batch insert
    void batchInsertShelters(List<ShelterCsvRowDto> rows);

}
