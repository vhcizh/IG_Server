package iGuard.Server.Controller;

import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kiosk")
public class KioskController {

    @Autowired
    KioskService ks;

    @Autowired
    ShelterRepository sr;


    @GetMapping
    public String showKioskPage(Model model) {
        return "kiosk";
    }

    @PostMapping("/enter")
    public String enterShelter(@RequestParam int shelterId, @RequestParam String phoneNumber, Model model) {
        try {
            ks.enter(shelterId, phoneNumber);
            model.addAttribute("message", "쉼터에 입장하셨습니다.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/kiosk";
    }

    @PostMapping("/exit")
    public String exitShelter(@RequestParam int shelterId, @RequestParam String phoneNumber, Model model) {
        try {
            ks.exit(shelterId, phoneNumber);
            model.addAttribute("message", "쉼터에서 나갑니다.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/kiosk";
    }



}
