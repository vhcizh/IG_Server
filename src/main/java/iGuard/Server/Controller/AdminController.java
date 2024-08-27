package iGuard.Server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/recommendShelterLocation")
    public String recommendShelterLocation() {
        return "recommendShelterLocation"; // 메인 페이지 템플릿 이름
    }
}
