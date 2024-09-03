package iGuard.Server.Controller;

import iGuard.Server.Entity.Place;
import iGuard.Server.Service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PlaceController {

    @Autowired
    private PlaceService ps;

    @GetMapping("/places")
    public String getPlaceNear(
            @RequestParam float lat,
            @RequestParam float lon,
            @RequestParam float range,
            Model model
    ) {
        List<Place> places = ps.getPlacesNear(lat, lon, range);
        model.addAttribute("places", places);

        return "places";
    }
}