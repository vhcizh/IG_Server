package iGuard.Server.Controller;

import iGuard.Server.Entity.Job;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Service.JobService;
import iGuard.Server.Service.JobUserService;
import iGuard.Server.Service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/job/user")
public class JobUserController {

    @Autowired
    private final JobUserService jobUserService;

    @Autowired
    private ShelterService ss;

    public JobUserController(JobUserService jobUserService) {
        this.jobUserService = jobUserService;
    }

    @GetMapping("/map")
    public String showJobsNearUser(@RequestParam float lat, @RequestParam float lon,  @RequestParam(defaultValue = "1") Integer userId, Model model) {
        List<Shelter> shelters = ss.getNearestShelters(lat, lon);
        model.addAttribute("shelters", shelters);
        model.addAttribute("userId", userId); // 유저 아이디를 뷰로 전달
        return "jobUserMap"; // 사용자용 일자리 조회 뷰
    }
    @GetMapping("/jobs/{shelterId}")
    @ResponseBody
    public List<Job> getJobsByShelter(@PathVariable Integer shelterId) {
        return jobUserService.getJobsByShelter(shelterId);
    }


    @PostMapping("/apply/{jobId}")
    @ResponseBody
    public ResponseEntity<String> applyJob(@PathVariable Integer jobId, @RequestParam Integer userId, @RequestParam String applicationText) {
        try {
            jobUserService.applyForJob(jobId, userId, applicationText);
            return ResponseEntity.ok("지원이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("지원 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

