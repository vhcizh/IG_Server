package iGuard.Server.Controller;

import iGuard.Server.Entity.CompanyUser;
import iGuard.Server.Repository.CompanyUserRepository;
import iGuard.Server.Service.EmailService;
import iGuard.Server.Service.admin.CompanyUserDetailsService;
import iGuard.Server.Service.admin.CompanyUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private CompanyUserRepository cr;

    @GetMapping("/register")
    public String register(){
        return "admin/company_register";
    }
    // 로그인 폼 표시
    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/company_login"; // login.html 템플릿을 반환
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String companyName,
                           Model model) {
        // 사용자가 이미 존재하는지 확인
        CompanyUser member = companyUserService.findByCompanyEmail(email);
        if(member != null) {
            if (member.isVerified()) {
                model.addAttribute("message", "이미 등록된 이메일입니다.");
                return "admin/company_register";
            } else {
                // 인증되지 않은 사용자 삭제
                companyUserService.deleteCompanyUser(member.getCompanyUserId());
            }
        }
        // 사용자 등록
        CompanyUser user = new CompanyUser();
        user.setCompanyEmail(email);
        user.setPassword(password);
        user.setCompanyName(companyName);
        user.setVerified(false);
        companyUserService.registerUser(user);

        // 인증 코드 생성 및 전송
        String code = emailService.generateVerificationCode();
        emailService.sendVerificationEmail(email, code);

        model.addAttribute("email", email);
        model.addAttribute("message", "인증메일이 보내졌습니다. 인증코드를 입력해 주세요.");
        return "admin/verify";
    }

    // 사용자 등록 폼 표시
    @PostMapping("/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String code,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        boolean isValid = emailService.verifyCode(email, code);

        if (isValid) {
            // 인증 성공 시 사용자 계정을 활성화
            CompanyUser user = cr.findByCompanyEmail(email);
            if (user != null) {
                user.setVerified(true);
                cr.save(user);
                redirectAttributes.addFlashAttribute("message", "성공적으로 인증되었습니다. 회원가입이 완료되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("message", "유효하지 않은 인증 코드입니다. 다시 시도해주세요");
            return "admin/company_register";
        }
        return "redirect:/admin/login";
    }
}
