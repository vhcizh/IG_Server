package iGuard.Server.Controller;

import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Service.PlaceService;
import iGuard.Server.Service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/common/places")
public class PlaceController {

    @Autowired
    private PlaceService ps;
    @Autowired
    private ShelterService ss;

    @GetMapping("")
    public String getPlaceNear(
            @RequestParam float lat,
            @RequestParam float lon,
            @RequestParam float range,
            Model model
    ) {
        List<Place> places = ps.getPlacesNear(lat, lon, range);
        List<Shelter> shelters = ss.getNearestShelters(lat,lon);

        model.addAttribute("places", places);
        model.addAttribute("shelters",shelters);

        return "common/places";
    }
}