package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.VisitedShelterResponse;

import java.util.List;

public interface VisitedShelterService {

    // 방문한 쉼터 등록
    void createVisitedShelter(Integer userId, Integer shelterId);

    List<VisitedShelterResponse> getVisitedShelter();
}
