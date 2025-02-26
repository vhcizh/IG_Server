package iGuard.Server.Repository;

import iGuard.Server.Dto.user.ShelterDistanceDto;
import iGuard.Server.Dto.user.ShelterSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterRepositoryCustom {

    // 쉼터 조회 (필터링 동적 조회)
    Page<ShelterDistanceDto> search(ShelterSearchDto searchDto, Pageable pageable);

}
