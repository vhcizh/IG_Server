package iGuard.Server.Controller;

import iGuard.Server.Dto.UserRequest;
//import iGuard.Server.Dto.UserResponse;
import iGuard.Server.Service.user.UserService;
import iGuard.Server.Service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "join"; // join.html로 이동
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
        userService.registerUser(userRequest);
        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:home"; // 리다이렉트 경로 수정
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    // 내 정보 페이지
    /*
    @GetMapping("/mypage/me")
    public String myInfoPage(Model model) {
        model.addAttribute("user", userService.getUser());
        return "myInfo"; // 사용자 정보를 보여줄 뷰 이름
    }
     */


}
