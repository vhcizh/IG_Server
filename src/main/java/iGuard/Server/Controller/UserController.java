package iGuard.Server.Controller;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/common")
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) throws IOException {

        // 이용약관
        String terms = new String(Files.readAllBytes((Paths.get("src/main/resources/terms.txt"))));
        terms = terms.replace("\n", "<br>");

        model.addAttribute("userRequest", new UserRequest());
        model.addAttribute("terms", terms);

        return "common/join"; // join.html로 이동
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UserRequest userRequest, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // 유효성 검사 오류가 있을 경우, join 페이지로 돌아감
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:join"; // join.html로 돌아감
        }
        try {
            userService.registerUser(userRequest);
        } catch(RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:join";
        }
        redirectAttributes.addFlashAttribute("message", "회원가입 완료되었습니다.");
        return "redirect:/common/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "common/login";
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
        return "redirect:/common/mypage/me";
    }

    // 회원 탈퇴
    @PostMapping("/mypage/me/delete")
    public String deleteMe(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        userService.deleteUser();
        // 로그아웃 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        redirectAttributes.addFlashAttribute("message", "회원탈퇴 되었습니다.");
        return "redirect:/common/login";
    }


}
