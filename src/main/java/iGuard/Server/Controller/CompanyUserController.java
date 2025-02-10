package iGuard.Server.Controller;

import iGuard.Server.Entity.CompanyUser;
import iGuard.Server.Service.admin.CompanyUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;

    @GetMapping("/mypage")
    public String getCompanyUser(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            CompanyUser companyUser = companyUserService.findByCompanyEmail(email);

            if (companyUser != null) {
                model.addAttribute("companyUser", companyUser);
                return "admin/company_mypage";
            }
        }
        return "redirect:/admin/login";
    }
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam int companyUserId,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 RedirectAttributes redirectAttributes) {
        Optional<CompanyUser> companyUserOpt = companyUserService.getCompanyUserById(companyUserId);

        if (companyUserOpt.isPresent()) {
            CompanyUser companyUser = companyUserOpt.get();

            // Check if the current password matches
            if (companyUserService.checkPassword(companyUser, currentPassword)) {
                // Update the password
                companyUserService.updatePassword(companyUser, newPassword);
                redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자를 찾을 수 없습니다.");
        }

        return "redirect:/admin/mypage";
    }

    @PostMapping("/mypage/delete")
    public String deleteCompanyUser(@RequestParam int companyUserId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        companyUserService.deleteCompanyUser(companyUserId);
        // 로그아웃 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        redirectAttributes.addFlashAttribute("message", "회원탈퇴 되었습니다.");
        return "redirect:/admin/login";
    }
}