package iGuard.Server.Service.user;

import iGuard.Server.Entity.VisitedShelter;

import java.util.List;
import java.util.Optional;

public interface VisitedShelterService {

    // 방문한 쉼터 등록
    public VisitedShelter createVisitedShelter(Integer shelterId, Integer userId);

    // 방문한 쉼터 리스트 조회
    public List<VisitedShelter> getVisitedShelter(Integer userId);

}
