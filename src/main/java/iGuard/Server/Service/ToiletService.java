package iGuard.Server.Service;

import iGuard.Server.Entity.Toilet;
import iGuard.Server.Repository.ToiletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToiletService {
    @Autowired
    private ToiletRepository toiletRepository;

    public List<Toilet> getNearestToilets(float latitude, float longitude) {
        List<Toilet> toilets = toiletRepository.findNearestToilets(latitude, longitude);
        return toilets;
    }
}