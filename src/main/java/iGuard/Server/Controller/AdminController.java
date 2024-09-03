package iGuard.Server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/shelters")
    public String recommendShelterLocation() {
        return "admin/recommendShelterLocation"; // 메인 페이지 템플릿 이름
    }
}