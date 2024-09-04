package iGuard.Server.Controller;

import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Service.FCMTokenService;
import iGuard.Server.Service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fcm")
public class FCMTokenController {

    @Autowired
    private FCMTokenService fcmTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @GetMapping("/token")
    public String getTokenPage(@RequestParam("userid") Integer userId, Model model) {
        String token = fcmTokenService.getToken(userId);
        model.addAttribute("token", token);
        model.addAttribute("userid", userId);
        return "fcm-token";
    }

    @PostMapping("/token")
    public String updateToken(@RequestParam("userid") Integer userId, @RequestParam String token, Model model) {
        try {
            fcmTokenService.updateToken(userId, token);
            model.addAttribute("message", "Token updated successfully.");
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
        }
        return "fcm-token"; // 업데이트 후 다시 표시할 페이지 이름
    }
}