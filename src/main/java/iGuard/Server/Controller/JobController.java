package iGuard.Server.Controller;


import iGuard.Server.Entity.Shelter;
import iGuard.Server.Service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private ShelterService ss;


    @GetMapping
    public String showJobmap(@RequestParam float lat, @RequestParam float lon,Model model){
        List<Shelter> shelters = ss.getNearestShelters(lat,lon);

        model.addAttribute("shelters",shelters);

        return "jobmap";
    }

}
