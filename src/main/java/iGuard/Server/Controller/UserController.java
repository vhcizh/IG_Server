package iGuard.Server.Controller;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Service.auth.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "join"; // join.html로 이동
    }

    // 회원 가입 처리 메서드
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
        userService.registerUser(userRequest);
        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:/api/home"; // 리다이렉트 경로 수정
    }

    // 홈 페이지 표시 메서드
    @GetMapping("/home")
    public String homePage() {
        return "home"; // home.html로 이동
    }

}
