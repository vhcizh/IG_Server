package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.ReviewDto;
import iGuard.Server.Dto.user.ReviewResponse;
import iGuard.Server.Dto.user.VisitedShelterResponse;

import java.util.List;

public interface VisitedShelterService {

    // 방문한 쉼터 등록
    void createVisitedShelter(Integer userId, Integer shelterId);

    // 방문한 쉼터 리스트 조회
    List<VisitedShelterResponse> getVisitedShelter();

    // 쉼터 리뷰 작성
    void createReview(ReviewDto reviewDto);

    // 쉼터 리뷰 리스트
    List<ReviewResponse> getMyReviews();
}
