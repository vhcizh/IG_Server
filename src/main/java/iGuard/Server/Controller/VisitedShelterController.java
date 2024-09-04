package iGuard.Server.Controller;

import iGuard.Server.Service.user.VisitedShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/common/mypage")
@RequiredArgsConstructor
public class VisitedShelterController {

    private final VisitedShelterService visitedShelterService;

    // 마이페이지 (방문한 쉼터 리스트 조회)
    @GetMapping("")
    public String getShelter(Model model) {
        model.addAttribute("shelters", visitedShelterService.getVisitedShelter());
        return "common/mypage";
    }

    // 방문한 쉼터 등록
    @PostMapping("/shelter")
    public String visitShelter(@RequestParam("userId") Integer userId, @RequestParam("shelterId") Integer shelterId) {
        visitedShelterService.createVisitedShelter(userId, shelterId);
        return "redirect:/common/mypage";
    }

    // 쉼터 리뷰 작성
    @PostMapping("/shelter/{shelterId}/review")
    public String writeReview() {
        return "";
    }

    // 내가 작성한 리뷰 조회 /mypage/shelter/reviews
    @GetMapping("/shelter/reviews")
    public String getMyReviews() {
        return "";
    }

    // 쉼터로그 작성

    // 쉼터로그 조회

    // 민원 작성

    // 민원 조회
}