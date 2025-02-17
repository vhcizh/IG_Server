package iGuard.Server.Controller;

import iGuard.Server.Dto.UserRequest;
import iGuard.Server.Dto.UserUpdate;
import iGuard.Server.Service.EmailService;
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
    private final EmailService emailService;

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
    public String updateMyInfo(@Valid @ModelAttribute UserUpdate userUpdate, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userUpdate);
            redirectAttributes.addFlashAttribute("message", "회원정보가 성공적으로 수정되었습니다.");
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
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

    // 비밀번호 찾기 페이지
    @GetMapping("/password")
    public String findPasswordPage() {
        return "common/password";
    }

    // 비밀번호 재설정
    @PostMapping("/password")
    public String resetPassword(@RequestParam("id") String userId,
                                @RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            userService.resetPassword(userId, email);
            redirectAttributes.addFlashAttribute("message","임시 비밀번호가 발급되었습니다. 로그인 후 새 비밀번호로 변경하세요.");
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/common/login";
    }

    // 이메일 인증코드 보내기
    @PostMapping("/email")
    public String sendEmail(@RequestParam String email,
                            RedirectAttributes redirectAttributes) {
        try {
            if(userService.isVerified()) {
                redirectAttributes.addFlashAttribute("error", "이미 인증이 완료되었습니다.");
                return "redirect:/common/mypage/me";
            }

            String code = emailService.generateVerificationCode();
            emailService.sendVerificationEmail(email, code);

            redirectAttributes.addFlashAttribute("message", "이메일이 성공적으로 발송되었습니다.");
            redirectAttributes.addFlashAttribute("showVerificationForm", true);
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", "이메일 발송에 실패했습니다.");
        }
        return "redirect:/common/mypage/me";
    }

    // 인증코드 검사
    @PostMapping("/email/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String code,
                         RedirectAttributes redirectAttributes) {
        try {
            boolean isValid = emailService.verifyCode(email, code);
            if(!isValid) {
                redirectAttributes.addFlashAttribute("error", "코드가 일치하지 않습니다.");
                redirectAttributes.addFlashAttribute("showVerificationForm", true);
                return "redirect:/common/mypage/me";
            }

            userService.verifyEmail(email);
            redirectAttributes.addFlashAttribute("message", "인증이 완료되었습니다.");
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", "인증 처리 중 오류가 발생했습니다.");
            redirectAttributes.addFlashAttribute("showVerificationForm", true);
        }
        return "redirect:/common/mypage/me";
    }

}
