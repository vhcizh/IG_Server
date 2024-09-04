package iGuard.Server.Service.user;

import iGuard.Server.Entity.Shelter;
import iGuard.Server.Entity.User;
import iGuard.Server.Entity.VisitedShelter;
import iGuard.Server.Repository.ShelterRepository;
import iGuard.Server.Repository.UserRepository;
import iGuard.Server.Repository.VisitedShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitedShelterServiceImpl implements VisitedShelterService {

    private VisitedShelterRepository visitedShelterRepository;

    private ShelterRepository shelterRepository; // Shelter Repository
    private UserRepository userRepository; // User Repository

    @Override
    public VisitedShelter createVisitedShelter(Integer shelterId, Integer userId) {
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VisitedShelter visitedShelter = new VisitedShelter();
        visitedShelter.setShelter(shelter);
        visitedShelter.setUser(user);

        return visitedShelterRepository.save(visitedShelter);
    }

    @Override
    public List<VisitedShelter> getVisitedShelter(Integer userId) {
        return visitedShelterRepository.findAllByUser_userid(userId);
    }
}