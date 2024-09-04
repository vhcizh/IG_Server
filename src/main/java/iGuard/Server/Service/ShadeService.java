package iGuard.Server.Service;

import iGuard.Server.Entity.Shade;
import iGuard.Server.Repository.ShadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShadeService {
    @Autowired
    private ShadeRepository shadeRepository;

    public List<Shade> getNearestShades(float latitude, float longitude) {
        List<Shade> shades = shadeRepository.findNearestShades(latitude, longitude);
        return shades;
    }
}