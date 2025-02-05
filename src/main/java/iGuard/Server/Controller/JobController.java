package iGuard.Server.Controller;


import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.JobApplication;
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

    @GetMapping("/applications")//없어도 되긴함...
    public String viewJobApplications(@RequestParam Integer jobId, Model model) {
        List<JobApplication> jobApplications = js.getJobApplicationsByJobId(jobId);
        model.addAttribute("applications", jobApplications);
        return "jobApplications";
    }

    @GetMapping("/shelter/{shelterId}")
    public String viewApplicationsByShelter(@PathVariable Integer shelterId, Model model) {
        List<JobApplication> jobapplications = js.getApplicationsByShelterId(shelterId);
        model.addAttribute("applications", jobapplications);
        model.addAttribute("shelterId", shelterId);
        return "jobApplications";
    }

    // 일자리 신청 합격/탈락 처리
    @PostMapping("/shelter/{shelterId}")
    public String updateApplicationStatus(@PathVariable Integer shelterId,@RequestParam Integer applicationId, @RequestParam boolean isAccepted, Model model) {
        js.updateApplicationStatus(applicationId, isAccepted);
        return "redirect:/job/shelter/"+shelterId; // 신청 목록으로 리다이렉트
    }

}
