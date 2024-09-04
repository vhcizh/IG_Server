package iGuard.Server.Controller;

import iGuard.Server.Entity.VisitedShelter;
import iGuard.Server.Service.user.VisitedShelterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/common")
public class VisitedShelterController {

    VisitedShelterService visitedShelterService;

    @PostMapping("/mypage/shelter")
    public VisitedShelter visitShelter(@RequestParam("userId") Integer userId, @RequestParam("shelterId") Integer shelterId) {
        return visitedShelterService.createVisitedShelter(userId, shelterId);
    }

    @GetMapping("/mypage/shelter")
    public List<VisitedShelter> getShelter(@RequestParam("userId") Integer userId) {
        return visitedShelterService.getVisitedShelter(userId);
    }
}