package iGuard.Server.Controller;

import iGuard.Server.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company/auth")
public class CompanyAuthController {

    @Autowired
    private EmailService emailService;

    // 사용자 등록 폼 표시
    @GetMapping("/register")
    public String showRegisterForm() {
        return "company_register"; // register.html 템플릿을 반환
    }

    // 사용자 등록 시 인증 이메일 발송
    @PostMapping("/register")
    public String register(@RequestParam String email, Model model) {
        String code = emailService.generateVerificationCode();
        emailService.sendVerificationEmail(email, code);
        model.addAttribute("message", "인증메일이 보내졌습니다.");
        return "company_register";
    }

    // 인증 폼 표시
    @GetMapping("/verify")
    public String showVerifyForm() {
        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String code, Model model) {
        boolean isValid = emailService.verifyCode(email, code);
        if (isValid) {
            System.out.println("인증되었습니다");
            model.addAttribute("message", "성공적으로 인증되었습니다.");
        } else {
            model.addAttribute("message", "유효하지 않습니다.");
        }
        return "verify";
    }
}