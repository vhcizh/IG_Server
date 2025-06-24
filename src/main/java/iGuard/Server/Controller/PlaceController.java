package iGuard.Server.Controller;

import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Service.PlaceService;
import iGuard.Server.Service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final ShelterService shelterService;

    private static final Float RANGE = 1f; // 1km

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/common/home";
    }

    @GetMapping("/common/home")
    public String getPlaceNear(
            @CookieValue(value = "latitude", defaultValue = "37.566") Float latitude,
            @CookieValue(value = "longitude", defaultValue = "126.978") Float longitude,
            Model model
    ) {

        List<Place> places = placeService.getPlacesNear(latitude, longitude, RANGE);
        List<Shelter> shelters = shelterService.getNearestShelters(latitude,longitude, RANGE);

        model.addAttribute("places", places);
        model.addAttribute("shelters",shelters);

        return "common/home";
    }

}