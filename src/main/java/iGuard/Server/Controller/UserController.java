package iGuard.Server.Controller;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/common")
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "common/join"; // join.html로 이동
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
        userService.registerUser(userRequest);
        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:home"; // 리다이렉트 경로 수정
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "common/login";
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String mypage() {
        return "common/mypage";
    }

    // 내 정보 페이지
    @GetMapping("/mypage/me")
    public String myInfoPage(Model model) {
        model.addAttribute("user", userService.getUser());
        return "common/myInfo";
    }

    // 내 정보 수정
    @PostMapping("/mypage/me")
    public String updateMyInfo(@Valid @ModelAttribute UserUpdate userRequest) {
        userService.updateUser(userRequest);
        return "redirect:mypage/me";
    }

    // 회원 탈퇴
    @DeleteMapping("/mypage/me")
    public String deleteMe() {
        userService.deleteUser();
        return "redirect:/home";
    }


}
