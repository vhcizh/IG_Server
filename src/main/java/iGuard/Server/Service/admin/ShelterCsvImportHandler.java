package iGuard.Server.Service.admin;

import iGuard.Server.Dto.ShelterCsvRowDto;
import iGuard.Server.Repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ShelterCsvImportHandler extends AbstractCsvImportHandler<ShelterCsvRowDto> {

    private final ShelterRepository shelterRepository;
    private static final String[] SHELTER_HEADERS = {"시설유형","쉼터명칭","소재지도로명주소","사용여부","시설면적","이용가능인원수","선풍기보유현황","에어컨보유현황","야간개방","휴일개방","숙박가능여부","특이사항","관리기관","관리기관전화번호","시설유형명","위도","경도"};

    @Override
    public String getFileType() {
        return "shelter";
    }

    @Override
    protected String[] getExpectedHeaders() {
        return SHELTER_HEADERS;
    }

    @Override
    protected Set<String> getExistingKeys() {
        return new HashSet<>(shelterRepository.findAllUniqueKeys());
    }

    @Override
    protected String extractUniqueKey(String[] row) {
        return row[1] + "|" + row[2]; // 쉼터명칭 + 주소
    }

    @Override
    protected ShelterCsvRowDto mapRowToDto(String[] row) {
        return ShelterCsvRowDto.from(row);
    }

    @Override
    protected void batchInsert(List<ShelterCsvRowDto> rows) {
        shelterRepository.batchInsertShelters(rows);
    }

}