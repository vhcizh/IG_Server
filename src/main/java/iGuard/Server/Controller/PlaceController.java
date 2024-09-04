package iGuard.Server.Controller;

import iGuard.Server.Entity.Place;
import iGuard.Server.Entity.Shade;
import iGuard.Server.Entity.Shelter;
import iGuard.Server.Entity.Toilet;
import iGuard.Server.Service.PlaceService;
import iGuard.Server.Service.ShadeService;
import iGuard.Server.Service.ShelterService;
import iGuard.Server.Service.ToiletService;
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
    @Autowired
    private ShelterService ss;

    @Autowired
    private ToiletService ts;

    @Autowired
    private ShadeService sh;

    @GetMapping("/places")
    public String getPlaceNear(
            @RequestParam float lat,
            @RequestParam float lon,
            @RequestParam float range,
            Model model
    ) {

        List<Place> places = ps.getPlacesNear(lat, lon, range);
        List<Shelter> shelters = ss.getNearestShelters(lat,lon);
        List<Toilet> nearbyToilets = ts.getNearestToilets(lat, lon);
        List<Shade> nearbyShades = sh.getNearestShades(lat, lon);


        model.addAttribute("places", places);
        model.addAttribute("shelters",shelters);
        model.addAttribute("toilets", nearbyToilets);
        model.addAttribute("shades", nearbyShades);

        return "places";
    }
}