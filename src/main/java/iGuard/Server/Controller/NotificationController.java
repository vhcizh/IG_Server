package iGuard.Server.Controller;

import iGuard.Server.Entity.User;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    // 알림 전송 페이지
    @GetMapping("/send")
    public String showSendNotificationPage(@RequestParam("userid") Integer userId, Model model) {
        Optional<User> optionalUser = userRepository.findByUserid(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "sendNotification"; // sendNotification.html 뷰를 반환
        } else {
            model.addAttribute("error", "Invalid user Id");
            return "notification_error"; // notification_error.html 뷰를 반환 또는 적절한 오류 처리
        }
    }

    // 알림 전송 처리
    @PostMapping("/send")
    public String sendNotification(@RequestParam("userid") Integer userId, @RequestParam("title") String title, @RequestParam("body") String body, Model model) {
        Optional<User> optionalUser = userRepository.findByUserid(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getFcmToken() != null) {
                firebaseMessagingService.sendNotification(user.getFcmToken(), title, body);
                model.addAttribute("message", "알림이 성공적으로 전송되었습니다.");
            } else {
                model.addAttribute("error", "사용자에게 유효한 FCM 토큰이 없습니다.");
            }
        } else {
            model.addAttribute("error", "Invalid user Id");
        }
        return "sendNotificationResult"; // sendNotificationResult.html 뷰를 반환
    }
}
