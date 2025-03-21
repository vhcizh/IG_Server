package iGuard.Server.Service;

import iGuard.Server.Entity.Shelter;
import iGuard.Server.Repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterService {

    @Autowired
    private ShelterRepository shelterRepository;

    public List<Shelter> getNearestShelters(float lat, float lon, float range) {

        return shelterRepository.findSheltersWithinRange(lat, lon, range);
    }
}