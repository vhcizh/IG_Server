package iGuard.Server.Controller;

import iGuard.Server.Dto.user.ShelterResponse;
import iGuard.Server.Dto.user.ShelterSearchDto;
import iGuard.Server.Service.user.ShelterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/common/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("")
    public String shelters(@ModelAttribute ShelterSearchDto searchDto,
                           @PageableDefault(sort = "shelterName") Pageable pageable,
                           Model model,
                           HttpServletRequest request) {

        if(searchDto.getLatitude() == null || searchDto.getLongitude() == null) {
            searchDto.setLatitude(getCookieValue(request, "latitude"));
            searchDto.setLongitude(getCookieValue(request, "longitude"));
        }

        Page<ShelterResponse> shelterPage = shelterService.getShelters(searchDto, pageable);

        model.addAttribute("shelters", shelterPage.getContent());
        model.addAttribute("page", shelterPage);
        model.addAttribute("searchDto", searchDto);  // 현재 검색 조건을 뷰에 전달
        model.addAttribute("request", request);

        // 시설 유형과 시/도 목록을 미리 가져와서 모델에 추가
        model.addAttribute("cities", shelterService.getAllCities());
        model.addAttribute("facilityTypes", shelterService.getFacilityType());

        // 페이지 버튼 클릭 시 서치 조건 유지
        // URL에 필요한 필터 값만 포함 (null 값 자동 제외)
        model.addAttribute("searchParams", setSearchParams(searchDto));

        return "common/shelters";
    }

    private Float getCookieValue(HttpServletRequest request, String name) {
        if(request.getCookies() == null) return null;

        Float reulst =  Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .map(value -> {
                    try {
                        return Float.parseFloat(value);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .orElse(null);

        System.out.println(name + " : " + reulst);
        return reulst;
    }

    private String setSearchParams(ShelterSearchDto searchDto) {
        return UriComponentsBuilder.fromPath("")
                .queryParamIfPresent("city", Optional.ofNullable(searchDto.getCity()))
                .queryParamIfPresent("gu", Optional.ofNullable(searchDto.getGu()))
                .queryParamIfPresent("facilityType", Optional.ofNullable(searchDto.getFacilityType()))
                .queryParamIfPresent("shelterName", Optional.ofNullable(searchDto.getShelterName()))
                .queryParamIfPresent("isOpenAtNight", Optional.ofNullable(searchDto.getIsOpenAtNight()))
                .queryParamIfPresent("allowsAccommodation", Optional.ofNullable(searchDto.getAllowsAccommodation()))
                .queryParamIfPresent("isOpenOnHolidays", Optional.ofNullable(searchDto.getIsOpenOnHolidays()))
                .queryParamIfPresent("sortBy", Optional.ofNullable(searchDto.getSortBy()))
                .queryParamIfPresent("longitude", Optional.ofNullable(searchDto.getLongitude()))
                .queryParamIfPresent("latitude", Optional.ofNullable(searchDto.getLatitude()))
                .toUriString();
    }

    // 검색 필터용 api
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
        ShelterResponse shelter = shelterService.getShelterById(shelterId);
        String encodedName = URLEncoder.encode(shelter.getShelterName(), StandardCharsets.UTF_8);

        model.addAttribute("shelter", shelter);
        model.addAttribute("encodedName", encodedName);
        model.addAttribute("ratingAvg", shelterService.getShelterReviewRatingAvg(shelterId));
        model.addAttribute("categoryCounts", shelterService.getShelterReviewCategoryCount(shelterId));
        return "common/shelter_details"; // 뷰 이름
    }
}
