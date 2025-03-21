package iGuard.Server.Controller;

import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Service.PlaceService;
import iGuard.Server.Service.ShelterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class PlaceController {
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/common/home";
    }

    @Autowired
    private PlaceService ps;
    @Autowired
    private ShelterService ss;

    private final Float SEOUL_LATITUDE = 37.566f;
    private final Float SEOUL_LONGITUDE = 126.978f;
    private final Float RANGE = 1f; // 1km

    @GetMapping("/common/home")
    public String getPlaceNear(
            HttpServletRequest request,
            Model model
    ) {
        Float latitude = getCookieValue(request, "latitude");
        Float longitude = getCookieValue(request, "longitude");

        if(latitude == null || longitude == null) {
            // 장소 데이터 없을 경우, 서울시청 기준
            latitude = SEOUL_LATITUDE;
            longitude = SEOUL_LONGITUDE;
        }

        List<Place> places = ps.getPlacesNear(latitude, longitude, RANGE);
        List<Shelter> shelters = ss.getNearestShelters(latitude,longitude, RANGE);

        model.addAttribute("places", places);
        model.addAttribute("shelters",shelters);

        return "common/home";
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
}