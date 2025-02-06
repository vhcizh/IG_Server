package iGuard.Server.Controller;

import iGuard.Server.Dto.user.ShelterResponse;
import iGuard.Server.Dto.user.ShelterSearchDto;
import iGuard.Server.Service.user.ShelterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/common/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("")
    public String shelters(@ModelAttribute ShelterSearchDto searchDto,
                           @PageableDefault(size = 10, sort = "shelterName") Pageable pageable,
                           Model model,
                           HttpServletRequest request) {

        // 사용자가 요청한 페이지 번호를 0으로 변환
        int pageNumber = pageable.getPageNumber(); // 0부터 시작하는 페이지 번호
        if (pageNumber > 0) {
            pageable = PageRequest.of(pageNumber - 1, pageable.getPageSize(), pageable.getSort());
        }

        Page<ShelterResponse> shelterPage = shelterService.getShelters(searchDto, pageable);

        model.addAttribute("shelters", shelterPage.getContent());
        model.addAttribute("page", shelterPage);
        model.addAttribute("searchDto", searchDto);  // 현재 검색 조건을 뷰에 전달
        model.addAttribute("request", request);

        return "/common/shelters";
    }

    @GetMapping("/facilities")
    @ResponseBody
    public List<String> getFacilityTypes() {
        return shelterService.getFacilityType();
    }

    @ResponseBody
    @GetMapping("/cities")
    public List<String> getCities() {
        return shelterService.getAllCities();
    }

    @ResponseBody
    @GetMapping("/gus")
    public List<String> getGus(@RequestParam String city) {
        return shelterService.getGus(city);
    }

    @ResponseBody
    @GetMapping("/dongs")
    public List<String> getDongs(@RequestParam String city, @RequestParam String gu) {
        return shelterService.getDongs(city, gu);
    }

    // 쉼터 상세 조회
    @GetMapping("/{shelterId}")
    public String getShelterDetails(@PathVariable Integer shelterId, Model model) {
        model.addAttribute("shelter", shelterService.getShelterById(shelterId));
        return "common/shelter_details"; // 뷰 이름
    }
}
