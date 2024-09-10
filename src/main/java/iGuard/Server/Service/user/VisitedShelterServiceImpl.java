package iGuard.Server.Service.user;

import iGuard.Server.Dto.user.ReviewDto;
import iGuard.Server.Dto.user.ReviewResponse;
import iGuard.Server.Dto.user.VisitedShelterResponse;
import iGuard.Server.Entity.*;
import iGuard.Server.Enum.ShelterPreference;
import iGuard.Server.Repository.*;
import iGuard.Server.Util.SecurityUserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitedShelterServiceImpl implements VisitedShelterService {

    private final VisitedShelterRepository visitedShelterRepository;
    private final SecurityUserContextProvider userContextProvider;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;

    @Override
    public void createVisitedShelter(Integer userId, Integer shelterId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        VisitedShelter visitedShelter = new VisitedShelter();
        visitedShelter.setShelter(shelter);
        visitedShelter.setUser(user);

        visitedShelterRepository.save(visitedShelter);
    }

    @Override
    public List<VisitedShelterResponse> getVisitedShelter() {
        String id = userContextProvider.getLoginUserId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return visitedShelterRepository
                .findAllByUser_id(id)
                .stream()
                .map( shelter -> new VisitedShelterResponse(
                        shelter.getId(),
                        shelter.getShelter(),
                        shelter.getDate().format(formatter),
                        shelter.getReviewed())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void createReview(ReviewDto dto) {

        String id = userContextProvider.getLoginUserId();
        User user = userRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(dto.getShelterId()==null) {
            throw new RuntimeException("shelterId is null");
        }
        Shelter shelter = shelterRepository.findById(dto.getShelterId())
                .orElseThrow(() -> new RuntimeException("Shelter not found"));

        // 1. reviewed false 여부 확인 및 true 설정
        if(dto.getVisitedShelterId()==null) {
            throw new RuntimeException("visitedShelterId is null");
        }

        VisitedShelter visitedShelter = visitedShelterRepository
                .findById(dto.getVisitedShelterId())
                .orElseThrow(() -> new RuntimeException("VisitedShelter not found"));
        if (visitedShelter.getReviewed()) {
            throw new RuntimeException("This is already reviewed");
        } else {
            visitedShelter.setReviewed(true);
        }

        // 2. 리뷰 생성
        Review review = new Review();
        review.setUser(user);
        review.setShelter(shelter);
        review.setRating(dto.getRating());
        reviewRepository.save(review);

        // 3. 리뷰 카테고리 생성
        if(!dto.getCategories().isEmpty()) {
            List<ReviewCategory> reviewCategories = dto.getCategories().stream()
                    .map(category -> {
                        ReviewCategory reviewCategory = new ReviewCategory();
                        reviewCategory.setCategory(category);
                        reviewCategory.setReview(review);
                        return reviewCategory;
                    })
                    .toList();
            reviewCategoryRepository.saveAll(reviewCategories);
        }


    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getMyReviews() {
        String id = userContextProvider.getLoginUserId();
        User user = userRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<ReviewResponse> reviewList = reviewRepository.findReviewListByUserId(user.getUserid());

        for (ReviewResponse review : reviewList) {
            List<ShelterPreference> categories = reviewRepository.findCategoriesByReviewId(review.getReviewId());
            review.setCategories(categories);
        }

        return reviewList;
    }

}