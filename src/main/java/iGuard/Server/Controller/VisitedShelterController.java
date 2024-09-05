package iGuard.Server.Controller;

import iGuard.Server.Dto.user.ReviewDto;
import iGuard.Server.Service.user.VisitedShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/common/mypage")
@RequiredArgsConstructor
public class VisitedShelterController {

    private final VisitedShelterService visitedShelterService;

    // 마이페이지 (방문한 쉼터 리스트 조회)
    @GetMapping("")
    public String getShelter(Model model) {
        model.addAttribute("shelters", visitedShelterService.getVisitedShelter());
        model.addAttribute("reviewDto", new ReviewDto());
        return "common/mypage";
    }

    // 방문한 쉼터 등록
    @PostMapping("/shelter")
    public String visitShelter(@RequestParam("userId") Integer userId, @RequestParam("shelterId") Integer shelterId) {
        visitedShelterService.createVisitedShelter(userId, shelterId);
        return "redirect:/common/mypage";
    }

    // 쉼터 리뷰 작성
    @PostMapping("/shelter/review")
    public String writeReview(@ModelAttribute ReviewDto reviewDto) {
        visitedShelterService.createReview(reviewDto);
        return "redirect:/common/mypage";
    }

    // 내가 작성한 리뷰
    @GetMapping("/shelter/reviews")
    public String getMyReviews(Model model) {
        model.addAttribute("reviews", visitedShelterService.getMyReviews());
        return "common/review";
    }

    // 쉼터로그 작성

    // 쉼터로그 조회

    // 민원 작성

    // 민원 조회
}