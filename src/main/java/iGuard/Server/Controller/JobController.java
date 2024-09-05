package iGuard.Server.Controller;


import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Enum.JobType;
import iGuard.Server.Service.JobService;
import iGuard.Server.Service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private ShelterService ss;

    @Autowired
    private final JobService js;


    @Autowired
    public JobController(JobService jobService) {
        this.js = jobService;
    }

    @GetMapping
    public String showJobmap(@RequestParam float lat, @RequestParam float lon,Model model){
        List<Shelter> shelters = ss.getNearestShelters(lat,lon);

        model.addAttribute("shelters",shelters);

        return "jobmap";
    }

    // 일자리 등록 페이지를 띄우기 위한 엔드포인트
    @GetMapping("/create/{shelterId}")
    public String showCreateJobPage(@PathVariable Integer shelterId, Model model) {
        model.addAttribute("shelterId", shelterId);
        model.addAttribute("jobTypes", JobType.values()); // JobType 목록을 제공
        return "createJob";
    }


    @PostMapping("/create")
    public String createJob(@RequestParam Integer shelterId, @RequestParam JobType jobType, Model model) {
        Job newJob = js.createJob(shelterId, jobType);
        model.addAttribute("job", newJob);
        return "jobCreatedPopupClose";
    }

}
