package iGuard.Server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/common/home";
    }

    @GetMapping("/common/home")
    public String home() {
        return "common/home";
    }
}
