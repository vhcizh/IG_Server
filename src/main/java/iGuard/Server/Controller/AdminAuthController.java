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

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private CompanyUserRepository cr;

    @Autowired
    private CompanyUserDetailsService cds;

    @GetMapping("/register")
    public String register(){
        return "company_register";
    }
    // 로그인 폼 표시
    @GetMapping("/login")
    public String showLoginForm() {
        return "company_login"; // login.html 템플릿을 반환
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request
    ) {
        CompanyUser user = companyUserService.findByCompanyEmail(email);

        if (user != null && companyUserService.checkPassword(user, password)) {
            // Create authentication token
            UserDetails userDetails = cds.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Set authentication in SecurityContext
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Redirect to home page
            return "redirect:mypage";
        } else {
            return "company_login";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String companyName,
                           Model model) {
        // 사용자가 이미 존재하는지 확인
        if (companyUserService.findByCompanyEmail(email) != null) {
            model.addAttribute("message", "이미 등록된 이메일입니다.");
            return "company_register";
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
        model.addAttribute("message", "인증메일이 보내졌습니다. 아래에 인증코드를 입력해 주세요.");
        return "verify";
    }

    // 사용자 등록 폼 표시
    @PostMapping("/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String code,
                         Model model) {
        boolean isValid = emailService.verifyCode(email, code);

        if (isValid) {
            // 인증 성공 시 사용자 계정을 활성화
            CompanyUser user = cr.findByCompanyEmail(email);
            if (user != null) {
                user.setVerified(true);
                cr.save(user);
                model.addAttribute("message", "성공적으로 인증되었습니다. 회원가입이 완료되었습니다.");
            } else {
                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("message", "유효하지 않습니다.");
        }
        model.addAttribute("email", email); // 이메일 주소를 유지
        return "home";
    }
}
