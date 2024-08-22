package iGuard.Server.Controller;

import iGuard.Server.Entity.CompanyUser;
import iGuard.Server.Service.*;
import iGuard.Server.Service.auth.CompanyUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Slf4j
@Controller
@RequestMapping("/admin/mypage")
public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;


    @GetMapping
    public String getCompanyUser(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            CompanyUser companyUser = companyUserService.findByCompanyEmail(email);

            if (companyUser != null) {
                model.addAttribute("companyUser", companyUser);
                return "company_mypage";
            }
        }
        return "redirect:/admin/login";
    }
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam int companyUserId,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Model model) {
        Optional<CompanyUser> companyUserOpt = companyUserService.getCompanyUserById(companyUserId);

        if (companyUserOpt.isPresent()) {
            CompanyUser companyUser = companyUserOpt.get();

            // Check if the current password matches
            if (companyUser.getPassword().equals(currentPassword)) {
                // Update the password
                companyUser.setPassword(newPassword);
                companyUserService.saveOrUpdateCompanyUser(companyUser);
                model.addAttribute("Message", "성공적으로 바뀌었습니다.");
            } else {
                model.addAttribute("Message", "현재 비밀번호와 다릅니다.");
            }
        } else {
            model.addAttribute("Message", "유저를 찾을 수 없습니다.");
        }

        return "redirect:/admin/mypage/?companyUserId=" + companyUserId;
    }

    @DeleteMapping
    public String deleteCompanyUser(@RequestParam int companyUserId) {
        companyUserService.deleteCompanyUser(companyUserId);
        return "redirect:/admin/mypage/?deleted=true";
    }
}