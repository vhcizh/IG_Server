package iGuard.Server.Controller;

import iGuard.Server.Dto.user.ReviewRequest;
import iGuard.Server.Service.user.UserService;
import iGuard.Server.Service.user.VisitedShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/common/mypage")
@RequiredArgsConstructor
public class VisitedShelterController {

    private final VisitedShelterService visitedShelterService;
    private final UserService userService;

    // 마이페이지 (방문한 쉼터 리스트 조회)
    @GetMapping("")
    public String getShelter(Model model) {
        model.addAttribute("shelters", visitedShelterService.getVisitedShelter());
        model.addAttribute("reviewRequest", new ReviewRequest());
        model.addAttribute("userId", userService.getUser().getUserId());
        return "common/mypage";
    }

    // 방문한 쉼터 등록
    @PostMapping("/shelter")
    public String visitShelter(@RequestParam("userId") Integer userId, @RequestParam("shelterId") Integer shelterId, RedirectAttributes redirectAttributes) {
        try {
            visitedShelterService.createVisitedShelter(userId, shelterId);
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/common/mypage";
    }

    // 쉼터 리뷰 작성
    @PostMapping("/shelter/review")
    public String writeReview(@ModelAttribute ReviewRequest reviewRequest, RedirectAttributes redirectAttributes) {
        try {
            visitedShelterService.createReview(reviewRequest);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
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